package com.example.imac.chs_pharmacy;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static java.util.UUID.fromString;

public class LinkCapActivity extends AppCompatActivity implements BleWrapperUiCallbacks{
    public static final String EXTRAS_DEVICE_NAME    = "BLE_DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "BLE_DEVICE_ADDRESS";
    public static final String EXTRAS_DEVICE_RSSI    = "BLE_DEVICE_RSSI";


    TextView tv1;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    TextView tv5;
    TextView tv6;


    private String mDeviceName;
    private String mDeviceAddress;
    private String mDeviceRSSI;

    private BleWrapper mBleWrapper;

    //holds array of all the services we have
    private ArrayList<BluetoothGattService> mBTServices;

    private TextView mDeviceNameView;
    private TextView mDeviceAddressView;
    private TextView mDeviceRssiView;
    private TextView mDeviceStatus;
    private ListView mListView;
    private View mListViewHeader;
    private TextView mHeaderTitle;
    private TextView mHeaderBackButton;
    private static final String TAG = "Patient";

    final static public UUID CAP_SERVICE  = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");
    final static public UUID CREATE_KEY   = UUID.fromString("00003001-0000-1000-8000-00805f9b34fb");
    final static public UUID CREATE_VALUE   = UUID.fromString("00003002-0000-1000-8000-00805f9b34fb");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_cap);

        mBTServices  = new ArrayList<BluetoothGattService>();

        //for now just display the name and rxid
        Patient p = Patient.getInstance();
        final String patName = p.getPatientName();
        String rxid = p.getRxid();

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        mDeviceRSSI = intent.getIntExtra(EXTRAS_DEVICE_RSSI, 0) + " db";

        tv2=(TextView)findViewById(R.id.textView2);
        tv2.setText(patName);

        tv1=(TextView)findViewById(R.id.textView1);
        tv1.setText(rxid);

        tv4=(TextView)findViewById(R.id.textView4);
        tv4.setText(mDeviceName);

        tv5=(TextView)findViewById(R.id.textView5);
        tv5.setText(mDeviceAddress);

        tv6=(TextView)findViewById(R.id.textView6);
        tv6.setText(mDeviceRSSI);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mBleWrapper == null) mBleWrapper = new BleWrapper(this, this);

        if(!mBleWrapper.initialize()) {
            finish();
        }

        // start automatically connecting to the device
        mBleWrapper.connect(mDeviceAddress);

        uiAvailableServices(mBleWrapper.getGatt(), mBleWrapper.getDevice(), mBleWrapper.getCachedServices());

    }



    //add each service to the services array
    public void uiAvailableServices(final BluetoothGatt gatt,
                                    final BluetoothDevice device,
                                    final List<BluetoothGattService> services)
    {
//                for(BluetoothGattService service : mBleWrapper.getCachedServices()) {
//                    mBTServices.add(service);
//                    //output service stuff here
//                    String uuid = service.getUuid().toString().toLowerCase(Locale.getDefault());
//                    Log.d(TAG, uuid);
//                }


        //services aren't showing up here
        for (BluetoothGattService service : services)
        {
//                    String serviceName = BleNamesResolver.resolveUuid(service.getUuid().toString());
//                    Log.d(TAG, serviceName);

            String uuid = service.getUuid().toString().toLowerCase(Locale.getDefault());
            Log.d(TAG, uuid);


//                    mBleWrapper.getCharacteristicsForService(service);

        }
    }



    //button is clicked, this is where it all starts
    public void confirmData(View v){
        //now with the device connected, we need to set the service
//        BluetoothGattService service = mBTServices.get(index);
//        mBleWrapper.getCharacteristicsForService(service);

    }

    public byte[] parseHexStringToBytes(final String hex) {
        String tmp = hex.substring(2).replaceAll("[^[0-9][a-f]]", "");
        byte[] bytes = new byte[tmp.length() / 2]; // every two letters in the string are one byte finally

        String part = "";

        for (int i = 0; i < bytes.length; ++i) {
            part = "0x" + tmp.substring(i * 2, i * 2 + 2);
            bytes[i] = Long.decode(part).byteValue();
        }

        return bytes;
    }



    public void uiDeviceConnected(final BluetoothGatt gatt,
                                  final BluetoothDevice device)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDeviceStatus.setText("connected");
                invalidateOptionsMenu();
            }
        });
    }

    public void uiDeviceDisconnected(final BluetoothGatt gatt,
                                     final BluetoothDevice device)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDeviceStatus.setText("disconnected");

            }
        });
    }

    @Override
    public void uiDeviceFound(BluetoothDevice device, int rssi, byte[] record) {
        // no need to handle that in this Activity (here, we are not scanning)
    }

    public void uiNewRssiAvailable(final BluetoothGatt gatt,
                                   final BluetoothDevice device,
                                   final int rssi)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDeviceRSSI = rssi + " db";
                mDeviceRssiView.setText(mDeviceRSSI);
            }
        });
    }

    public void uiGotNotification(final BluetoothGatt gatt,
                                  final BluetoothDevice device,
                                  final BluetoothGattService service,
                                  final BluetoothGattCharacteristic ch)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // at this moment we only need to send this "signal" do characteristic's details view
//                mCharDetailsAdapter.setNotificationEnabledForService(ch);
            }
        });
    }






    public void uiCharacteristicForService(final BluetoothGatt gatt,
                                           final BluetoothDevice device,
                                           final BluetoothGattService service,
                                           final List<BluetoothGattCharacteristic> chars)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                mCharacteristicsListAdapter.clearList();
//                mListType = LinkCapActivity.ListType.GATT_CHARACTERISTICS;
//                mListView.setAdapter(mCharacteristicsListAdapter);
//                mHeaderTitle.setText(BleNamesResolver.resolveServiceName(service.getUuid().toString().toLowerCase(Locale.getDefault())) + "\'s characteristics:");
//                mHeaderBackButton.setVisibility(View.VISIBLE);
//
//                for(BluetoothGattCharacteristic ch : chars) {
//                    mCharacteristicsListAdapter.addCharacteristic(ch);
//                }
//                mCharacteristicsListAdapter.notifyDataSetChanged();
            }
        });
    }

    public void uiCharacteristicsDetails(final BluetoothGatt gatt,
                                         final BluetoothDevice device,
                                         final BluetoothGattService service,
                                         final BluetoothGattCharacteristic characteristic)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                mListType = LinkCapActivity.ListType.GATT_CHARACTERISTIC_DETAILS;
//                mListView.setAdapter(mCharDetailsAdapter);
//                mHeaderTitle.setText(BleNamesResolver.resolveCharacteristicName(characteristic.getUuid().toString().toLowerCase(Locale.getDefault())) + "\'s details:");
//                mHeaderBackButton.setVisibility(View.VISIBLE);
//
//                mCharDetailsAdapter.setCharacteristic(characteristic);
//                mCharDetailsAdapter.notifyDataSetChanged();
            }
        });
    }

    public void uiNewValueForCharacteristic(final BluetoothGatt gatt,
                                            final BluetoothDevice device,
                                            final BluetoothGattService service,
                                            final BluetoothGattCharacteristic characteristic,
                                            final String strValue,
                                            final int intValue,
                                            final byte[] rawValue,
                                            final String timestamp)
    {
//        if(mCharDetailsAdapter == null || mCharDetailsAdapter.getCharacteristic(0) == null) return;
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mCharDetailsAdapter.newValueForCharacteristic(characteristic, strValue, intValue, rawValue, timestamp);
//                mCharDetailsAdapter.notifyDataSetChanged();
//            }
//        });
    }

    public void uiSuccessfulWrite(final BluetoothGatt gatt,
                                  final BluetoothDevice device,
                                  final BluetoothGattService service,
                                  final BluetoothGattCharacteristic ch,
                                  final String description)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Writing to " + description + " was finished successfully!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void uiFailedWrite(final BluetoothGatt gatt,
                              final BluetoothDevice device,
                              final BluetoothGattService service,
                              final BluetoothGattCharacteristic ch,
                              final String description)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Writing to " + description + " FAILED!", Toast.LENGTH_LONG).show();
            }
        });
    }

}