package com.example.imac.chs_pharmacy;

import android.bluetooth.BluetoothAdapter;
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

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static java.util.UUID.fromString;

public class LinkCapActivity extends AppCompatActivity {

    //these are the only three things needed from scanning activity
    public static final String EXTRAS_DEVICE_NAME    = "BLE_DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "BLE_DEVICE_ADDRESS";
    public static final String EXTRAS_DEVICE_RSSI    = "BLE_DEVICE_RSSI";

    //initilazie the ble wrapper, pulling in functions that we need
    private BleWrapper mBleWrapper = null;


    TextView tv1;
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

    private static final String TAG = "Patient";

    final static public UUID hard_service  = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");
    final static public UUID new_key   = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
    final static public UUID assign_key   = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");


    @Override
    //put things here that don't need to be re-initialized
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_cap);
//
//        //constructor for the blewrapper
//        mBleWrapper = new BleWrapper(this, new BleWrapperUiCallbacks.Null()
//        {
//        });
//
//        //make sure ble hardware is accessable
//        if (!mBleWrapper.checkBleHardwareAvailable())
//        {
//            Toast.makeText(this, "No BLE-compatible hardware detected",
//                    Toast.LENGTH_SHORT).show();
//
//            //kill app if not accessable
//            finish();
//        }


        Patient p = Patient.getInstance();
        final String patName = p.getPatientName();
        String rxid = p.getRxid();

        Button btn = (Button) findViewById(R.id.confirm);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                byte[] writeKeyName = parseHexStringToBytes("age");
                byte[] writeValueName = parseHexStringToBytes(patName);

                BluetoothGatt gatt;

                gatt = mBleWrapper.getGatt();


                BluetoothGattCharacteristic k;
                BluetoothGattCharacteristic value;

                //service is the 180b
                //characteristic is what we need to write to
                //will need to write to two services. one for the key, the other for the value

                //write to key
                k = gatt.getService(hard_service).getCharacteristic(new_key);
                mBleWrapper.writeDataToCharacteristic(k, writeKeyName);

                //now to write the value
                value = gatt.getService(hard_service).getCharacteristic(new_key);
                mBleWrapper.writeDataToCharacteristic(value, writeValueName);

            }});

        final Intent intent = getIntent();
//        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
//        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
//        mDeviceRSSI = intent.getIntExtra(EXTRAS_DEVICE_RSSI, 0) + " db";

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

}




