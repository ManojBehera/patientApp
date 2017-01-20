package com.example.imac.chs_pharmacy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.Manifest;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import static android.content.ContentValues.TAG;

public class ScanningActivity extends ListActivity {
	
	private static final long SCANNING_TIMEOUT = 5 * 1000; /* 5 seconds */
	private static final int ENABLE_BT_REQUEST_ID = 1;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
	private boolean mScanning = false;
	private Handler mHandler = new Handler();
	private DeviceListAdapter mDevicesListAdapter = null;
	private BleWrapper mBleWrapper = null;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    protected LocationSettingsRequest mLocationSettingsRequest;
//    protected Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("needs location access");
                builder.setMessage("please grant location access");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }

                });
                builder.show();
            }
        }

        // create BleWrapper with empty callback object except uiDeficeFound function (we need only that here)
        mBleWrapper = new BleWrapper(this, new BleWrapperUiCallbacks.Null() {
            @Override
            public void uiDeviceFound(final BluetoothDevice device, final int rssi, final byte[] record) {
                handleFoundDevice(device, rssi, record);
            }
        });

        // check if we have BT and BLE on board

        if (mBleWrapper.checkBleHardwareAvailable() == false) {
            bleMissing();
        }


    }

        @Override
        public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
            switch (requestCode) {
                case PERMISSION_REQUEST_COARSE_LOCATION: {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "coarse granted");
                    }
                    else {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("funcitionality limited");
                        builder.setMessage("location hasn't been granted");
                        builder.setPositiveButton(android.R.string.ok, null);
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener(){
                            @Override
                            public void onDismiss(DialogInterface dialog) {

                            }
                        });
                        builder.show();
                    }
                    return;
                }
            }
        }



    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(ScanningActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }


    @Override
    protected void onResume() {
    	super.onResume();

    	// on every Resume check if BT is enabled (user could turn it off while app was in background etc.)
    	if(mBleWrapper.isBtEnabled() == false) {
			// BT is not turned on - ask user to make it enabled
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, ENABLE_BT_REQUEST_ID);
		    // see onActivityResult to check what is the status of our request
		}

    	// initialize BleWrapper object
        mBleWrapper.initialize();

    	mDevicesListAdapter = new DeviceListAdapter(this);
        setListAdapter(mDevicesListAdapter);

        // Automatically start scanning for devices
    	mScanning = true;
		// remember to add timeout for scanning to not run it forever and drain the battery
		addScanningTimeout();
		mBleWrapper.startScanning();

        invalidateOptionsMenu();
    };

    @Override
    protected void onPause() {
    	super.onPause();
    	mScanning = false;
    	mBleWrapper.stopScanning();
    	invalidateOptionsMenu();

    	mDevicesListAdapter.clearList();
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.scanning, menu);

        if (mScanning) {
            menu.findItem(R.id.scanning_start).setVisible(false);
            menu.findItem(R.id.scanning_stop).setVisible(true);
            menu.findItem(R.id.scanning_indicator)
                .setActionView(R.layout.progress_indicator);

        } else {
            menu.findItem(R.id.scanning_start).setVisible(true);
            menu.findItem(R.id.scanning_stop).setVisible(false);
            menu.findItem(R.id.scanning_indicator).setActionView(null);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scanning_start:
            	mScanning = true;
            	mBleWrapper.startScanning();
                break;
            case R.id.scanning_stop:
            	mScanning = false;
            	mBleWrapper.stopScanning();
                break;
        }

        invalidateOptionsMenu();
        return true;
    }


    /* user has selected one of the device */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        final BluetoothDevice device = mDevicesListAdapter.getDevice(position);
        if (device == null) return;

        final Intent intent = new Intent(this, PeripheralActivity.class);
        intent.putExtra(PeripheralActivity.EXTRAS_DEVICE_NAME, device.getName());
        intent.putExtra(PeripheralActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
        intent.putExtra(PeripheralActivity.EXTRAS_DEVICE_RSSI, mDevicesListAdapter.getRssi(position));

        if (mScanning) {
            mScanning = false;
            invalidateOptionsMenu();
            mBleWrapper.stopScanning();
        }

        startActivity(intent);
    }

    /* check if user agreed to enable BT */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // user didn't want to turn on BT
        if (requestCode == ENABLE_BT_REQUEST_ID) {
        	if(resultCode == Activity.RESULT_CANCELED) {
		    	btDisabled();
		        return;
		    }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

	/* make sure that potential scanning will take no longer
	 * than <SCANNING_TIMEOUT> seconds from now on */
	private void addScanningTimeout() {
		Runnable timeout = new Runnable() {
            @Override
            public void run() {
            	if(mBleWrapper == null) return;
                mScanning = false;
                mBleWrapper.stopScanning();
                invalidateOptionsMenu();
            }
        };
        mHandler.postDelayed(timeout, SCANNING_TIMEOUT);
	}

	/* add device to the current list of devices */
    private void handleFoundDevice(final BluetoothDevice device,
            final int rssi,
            final byte[] scanRecord)
	{
		// adding to the UI have to happen in UI thread
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mDevicesListAdapter.addDevice(device, rssi, scanRecord);
				mDevicesListAdapter.notifyDataSetChanged();
			}
		});
	}

    private void btDisabled() {
    	Toast.makeText(this, "Sorry, BT has to be turned ON for us to work!", Toast.LENGTH_LONG).show();
        finish();
    }

    private void bleMissing() {
    	Toast.makeText(this, "BLE Hardware is required but not available!", Toast.LENGTH_LONG).show();
        finish();
    }
}
