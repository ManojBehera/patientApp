package com.example.imac.chs_pharmacy;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.imac.chs_pharmacy.BleWrapper;
import com.example.imac.chs_pharmacy.BleWrapperUiCallbacks;
import com.example.imac.chs_pharmacy.R;

import java.util.ArrayList;
import android.os.Handler;


public class ScanningActivity extends ListActivity {
    private BleWrapper mBleWrapper = null;
    private DeviceListAdapter mDevicesListAdapter = null;
    private static final int ENABLE_BT_REQUEST_ID = 1;
    private static final long SCANNING_TIMEOUT = 5 * 1000; /* 5 seconds */
    private Handler mHandler = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create BleWrapper with empty callback object except uiDeficeFound function (we need only that here)
        mBleWrapper = new BleWrapper(this, new BleWrapperUiCallbacks.Null() {
            @Override
            public void uiDeviceFound(final BluetoothDevice device, final int rssi, final byte[] record) {
                handleFoundDevice(device, rssi, record);
            }
        });

        // check if we have BT and BLE on board
        if(!mBleWrapper.checkBleHardwareAvailable()) {
            bleMissing();
        }
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

    @Override
    //this is where the code goes when it wakes up
    //this method is not the same as the ble demo
    protected void onResume() {
        super.onResume();

        // check for Bluetooth enabled on each resume
        if (!mBleWrapper.isBtEnabled())
        {
            // Bluetooth is not enabled. Request to user to turn it on
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);
            finish();
        }

        // init ble wrapper
        mBleWrapper.initialize();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mBleWrapper.diconnect();
        mBleWrapper.close();
    }

    //this extends teh blewrapper. so, if you need to do something
    //when the device is disconnected, override  uidevicedisconnected

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_scan:
                mBleWrapper.startScanning();
                break;

            case R.id.action_stop:
                mBleWrapper.stopScanning();
                break;
        }

        invalidateOptionsMenu();
        return true;
    }

    /* user has selected one of the device */
    //this now needs to just program 
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        final BluetoothDevice device = mDevicesListAdapter.getDevice(position);
        if (device == null) return;

        final Intent intent = new Intent(this, ScanningActivity.class);
        intent.putExtra(ScanningActivity.EXTRAS_DEVICE_NAME, device.getName());
        intent.putExtra(ScanningActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
        intent.putExtra(ScanningActivity.EXTRAS_DEVICE_RSSI, mDevicesListAdapter.getRssi(position));


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



    private void bleMissing() {
        Toast.makeText(this, "BLE Hardware is required but not available!", Toast.LENGTH_LONG).show();
        finish();
    }

    private void btDisabled() {
        Toast.makeText(this, "Sorry, BT has to be turned ON for us to work!", Toast.LENGTH_LONG).show();
        finish();
    }


    /* make sure that potential scanning will take no longer
     * than <SCANNING_TIMEOUT> seconds from now on */
    private void addScanningTimeout() {
        Runnable timeout = new Runnable() {
            @Override
            public void run() {
                if(mBleWrapper == null) return;
//                mScanning = false;
                mBleWrapper.stopScanning();
                invalidateOptionsMenu();
            }
        };
        mHandler.postDelayed(timeout, SCANNING_TIMEOUT);
    }

}
