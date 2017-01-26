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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;
import java.util.Locale;

public class LinkCapActivity extends AppCompatActivity implements BleWrapperUiCallbacks{
    public static final String EXTRAS_DEVICE_NAME    = "BLE_DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "BLE_DEVICE_ADDRESS";
    public static final String EXTRAS_DEVICE_RSSI    = "BLE_DEVICE_RSSI";


    TextView tv;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    TextView tv5;
    TextView tv6;



    public enum ListType {
        GATT_SERVICES,
        GATT_CHARACTERISTICS,
        GATT_CHARACTERISTIC_DETAILS
    }

    private LinkCapActivity.ListType mListType = LinkCapActivity.ListType.GATT_SERVICES;
    private String mDeviceName;
    private String mDeviceAddress;
    private String mDeviceRSSI;

    private BleWrapper mBleWrapper;

    private TextView mDeviceNameView;
    private TextView mDeviceAddressView;
    private TextView mDeviceRssiView;
    private TextView mDeviceStatus;
    private ListView mListView;
    private View mListViewHeader;
    private TextView mHeaderTitle;
    private TextView mHeaderBackButton;
    private ServicesListAdapter mServicesListAdapter = null;
    private CharacteristicsListAdapter mCharacteristicsListAdapter = null;
    private CharacteristicDetailsAdapter mCharDetailsAdapter = null;
    private static final String TAG = "Patient";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_cap);


        Patient p = Patient.getInstance();
        String patName = p.getPatientName();
        String rxid = p.getRxid();

        connectViewsVariables();

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        mDeviceRSSI = intent.getIntExtra(EXTRAS_DEVICE_RSSI, 0) + " db";

        tv2=(TextView)findViewById(R.id.textView2);
        tv2.setText(patName);


        tv3=(TextView)findViewById(R.id.textView3);
        tv3.setText(rxid);

        tv4=(TextView)findViewById(R.id.textView4);
        tv4.setText(mDeviceName);

        tv5=(TextView)findViewById(R.id.textView5);
        tv5.setText(mDeviceAddress);

        tv6=(TextView)findViewById(R.id.textView6);
        tv6.setText(mDeviceRSSI);

    }


    public void confirmData(View view) {

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
                mServicesListAdapter.clearList();
                mCharacteristicsListAdapter.clearList();
                mCharDetailsAdapter.clearCharacteristic();

                invalidateOptionsMenu();

                mHeaderTitle.setText("");
                mHeaderBackButton.setVisibility(View.INVISIBLE);
                mListType = LinkCapActivity.ListType.GATT_SERVICES;
                mListView.setAdapter(mServicesListAdapter);
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
                mCharDetailsAdapter.setNotificationEnabledForService(ch);
            }
        });
    }





    public void uiAvailableServices(final BluetoothGatt gatt,
                                    final BluetoothDevice device,
                                    final List<BluetoothGattService> services)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mServicesListAdapter.clearList();
                mListType = LinkCapActivity.ListType.GATT_SERVICES;
                mListView.setAdapter(mServicesListAdapter);
                mHeaderTitle.setText(mDeviceName + "\'s services:");
                mHeaderBackButton.setVisibility(View.INVISIBLE);

                for(BluetoothGattService service : mBleWrapper.getCachedServices()) {
                    mServicesListAdapter.addService(service);
                }
                mServicesListAdapter.notifyDataSetChanged();
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
                mCharacteristicsListAdapter.clearList();
                mListType = LinkCapActivity.ListType.GATT_CHARACTERISTICS;
                mListView.setAdapter(mCharacteristicsListAdapter);
                mHeaderTitle.setText(BleNamesResolver.resolveServiceName(service.getUuid().toString().toLowerCase(Locale.getDefault())) + "\'s characteristics:");
                mHeaderBackButton.setVisibility(View.VISIBLE);

                for(BluetoothGattCharacteristic ch : chars) {
                    mCharacteristicsListAdapter.addCharacteristic(ch);
                }
                mCharacteristicsListAdapter.notifyDataSetChanged();
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
                mListType = LinkCapActivity.ListType.GATT_CHARACTERISTIC_DETAILS;
                mListView.setAdapter(mCharDetailsAdapter);
                mHeaderTitle.setText(BleNamesResolver.resolveCharacteristicName(characteristic.getUuid().toString().toLowerCase(Locale.getDefault())) + "\'s details:");
                mHeaderBackButton.setVisibility(View.VISIBLE);

                mCharDetailsAdapter.setCharacteristic(characteristic);
                mCharDetailsAdapter.notifyDataSetChanged();
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
        if(mCharDetailsAdapter == null || mCharDetailsAdapter.getCharacteristic(0) == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCharDetailsAdapter.newValueForCharacteristic(characteristic, strValue, intValue, rawValue, timestamp);
                mCharDetailsAdapter.notifyDataSetChanged();
            }
        });
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

    private void connectViewsVariables() {
//        mDeviceNameView = (TextView) findViewById(R.id.peripheral_name);
//        mDeviceAddressView = (TextView) findViewById(R.id.peripheral_address);
//        mDeviceRssiView = (TextView) findViewById(R.id.peripheral_rssi);
//        mDeviceStatus = (TextView) findViewById(R.id.peripheral_status);
////        mListView = (ListView) findViewById(R.id.listView);
//        mHeaderTitle = (TextView) mListViewHeader.findViewById(R.id.peripheral_service_list_title);
//        mHeaderBackButton = (TextView) mListViewHeader.findViewById(R.id.peripheral_list_service_back);
    }
}
