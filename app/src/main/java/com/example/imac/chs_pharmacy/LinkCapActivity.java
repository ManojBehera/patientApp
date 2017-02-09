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
    private String patientName;
    private String dosage;
    private String schedule;
    private String rxid;
    private String address;

    private BleWrapper mBleWrapper;

    //holds array of all the services we have
    private ArrayList<BluetoothGattService> mBTServices;
    private ArrayList<BluetoothGattCharacteristic> mCharacteristics;

    private TextView mDeviceNameView;
    private TextView mDeviceAddressView;
    private TextView mDeviceRssiView;
    private TextView mDeviceStatus;
    private ListView mListView;
    private View mListViewHeader;
    private TextView mHeaderTitle;
    private TextView mHeaderBackButton;

    //we know that the first characteristic will be 3000
    private String curCharacteristic = "3001";
    private String scheduleTimeString;


    private String curWriteValue;
    public UUID thisUUID;
    private static final String TAG = "LinkCap";
    public BluetoothGattCharacteristic charKey;
    public BluetoothGattCharacteristic charValue;
    public BluetoothGattCharacteristic timeZone;
    public BluetoothGattCharacteristic doseSchedule;
    public BluetoothGattCharacteristic verify;

    public BluetoothGattCharacteristic thisChar;

    final static public UUID CAP_SERVICE  = UUID.fromString("0000180b-0000-1000-8000-00805f9b34fb");
    final static public UUID CREATE_KEY   = UUID.fromString("00003001-0000-1000-8000-00805f9b34fb");
    final static public UUID CREATE_VALUE   = UUID.fromString("00003000-0000-1000-8000-00805f9b34fb");
    final static public UUID TIME_ZONE   = UUID.fromString("00003005-0000-1000-8000-00805f9b34fb");
    final static public UUID SCHEDULE   = UUID.fromString("00003006-0000-1000-8000-00805f9b34fb");
    final static public UUID VERIFY_CHAR   = UUID.fromString("00003007-0000-1000-8000-00805f9b34fb");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_cap);

        //for now just display the name and rxid
        Patient p = Patient.getInstance();
        patientName = p.getPatientName();
        rxid = p.getRxid();
        address = p.getAddress();
        dosage = p.getDosage();
        schedule = p.getLabel();

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        mDeviceRSSI = intent.getIntExtra(EXTRAS_DEVICE_RSSI, 0) + " db";

        tv2=(TextView)findViewById(R.id.textView2);
        tv2.setText(patientName);

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
    }


    //button is clicked, this is where it all starts
    public void confirmData(View v){
        if (mBleWrapper.connect(mDeviceAddress)) {
            Log.d(TAG, "we are connected!");
            Toast.makeText(getApplicationContext(), "Gathering data...", Toast.LENGTH_LONG).show();

        }

    }

    //add each service to the services array
    public void uiAvailableServices(final BluetoothGatt gatt,
                                    final BluetoothDevice device,
                                    final List<BluetoothGattService> services)
    {
        for (BluetoothGattService service : services)
        {
            if (service.getUuid().equals(CAP_SERVICE)) {
                //found the cap, now get the characteristics
                mBleWrapper.getCharacteristicsForService(service);
                Log.d(TAG, "FOUND THE CAP");
            }
                //add each service to our services array
            String uuid = service.getUuid().toString().toLowerCase(Locale.getDefault());
            Log.d(TAG, uuid + "available service");
//                mBTServices.add(service);


        }
    }


    //this changes between 3000 and 3001
    //this function needs to save both characteristics. it doesn't matter which one is set right now
    public void uiCharacteristicForService(final BluetoothGatt gatt,
                                           final BluetoothDevice device,
                                           final BluetoothGattService service,
                                           final List<BluetoothGattCharacteristic> chars)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "we are getting characteristics");

                for(BluetoothGattCharacteristic ch : chars) {

                    //Store the characteristics we need
                    if (ch.getUuid().equals(CREATE_KEY))
                    {
                        Log.d(TAG,  "found 3001");
                        charKey = ch;
                    }

                    if (ch.getUuid().equals(CREATE_VALUE))
                    {
                        Log.d(TAG,  "found 3000");
                        charValue = ch;
                    }

                    if (ch.getUuid().equals(TIME_ZONE))
                    {
                        Log.d(TAG,  "found 3005");
                        timeZone = ch;
                    }

                    if (ch.getUuid().equals(SCHEDULE))
                    {
                        Log.d(TAG,  "found 3006");
                        doseSchedule = ch;
                    }

                    if (ch.getUuid().equals(VERIFY_CHAR))
                    {
                        Log.d(TAG,  "found 3007");
                        verify = ch;
                    }

                    String uuid = ch.getUuid().toString().toLowerCase(Locale.getDefault());
                    Log.d(TAG, uuid + "characteristic");

                }

                //this function should only be called once. store 3000, but act on 3001 right now
                //we have our characteristics stored, now lets initiate the first one
                uiCharacteristicsDetails(mBleWrapper.getGatt(), mBleWrapper.getDevice(), mBleWrapper.getCachedService(), charKey);

            }
        });
    }

    //this is called when we find the characteristic we want to write to. right now will be 3000 or 3001
    public void uiCharacteristicsDetails(final BluetoothGatt gatt,
                                         final BluetoothDevice device,
                                         final BluetoothGattService service,
                                         final BluetoothGattCharacteristic characteristic)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //write to the variable characteristic with variable value

                //here we need to call a function that gives us the current write value
                //call the patient class to get the next write value

                //now we are going to write the characteristic
                //call patient to get the one we need to write to first
                Patient p = Patient.getInstance();
                curWriteValue = p.getNextWriteValue();

                if (curWriteValue == "none") {
                    //we're done here, now we need to write 3005 and 3006
                    //write to 3005

                    //the final call, the scheduling
                    if (curCharacteristic == "3006") {
                        Log.d(TAG, "we are at 3006, getting schedule times");
                        scheduleTimeString = p.getScheduleTimes();
                        mBleWrapper.writeDataToCharacteristic(characteristic, scheduleTimeString);
                    }
                    //now we gotta start checking values
                    else if (curCharacteristic == "3007")
                    {
                        mBleWrapper.requestCharacteristicValue(verify);
                    }
                    else {
                        Log.d(TAG, "we are at 3005");
                        curCharacteristic = "3005";
                        Long tsLong = System.currentTimeMillis() / 1000;
                        //just use the 7 offset for now
                        String ts = tsLong.toString() + "07";
                        mBleWrapper.writeDataToCharacteristic(timeZone, ts);

                    }
                }

                else {
                    mBleWrapper.writeDataToCharacteristic(characteristic, curWriteValue);
                }
            }
        });
    }

    //this should be called after writedatatocharacteristic
    public void uiSuccessfulWrite(final BluetoothGatt gatt,
                                  final BluetoothDevice device,
                                  final BluetoothGattService service,
                                  final BluetoothGattCharacteristic ch,
                                  final String description)
    {

        //start the next characteristic write. need to change the characteristic to the other one
        if (curCharacteristic == "3000"){
            Log.d(TAG, "just wrote to 3000. changing to char 3001");
            curCharacteristic = "3001";
             thisChar = charKey;
        }

        else if (curCharacteristic == "3001"){
            Log.d(TAG, "just wrote to 3001. changing to char 3000");
            curCharacteristic = "3000";
             thisChar = charValue;
        }

        //this is being called twice
        else if (curCharacteristic == "3006") {
            //done. now we start checking the values
            curCharacteristic = "3007";
//            mBleWrapper.diconnect();

        }
        //instead of closing now we need to write all the schedule times
       else if (curCharacteristic == "3005") {
            //now we need to write to 3006
            curCharacteristic = "3006";
            thisChar = doseSchedule;
        }



        //call the next write funciton. we need to
        uiCharacteristicsDetails(mBleWrapper.getGatt(), mBleWrapper.getDevice(), mBleWrapper.getCachedService(), thisChar);
    }


    public void uiDeviceConnected(final BluetoothGatt gatt,
                                  final BluetoothDevice device)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "in uiDeviceConnected");
            }
        });
    }

    public void uiDeviceDisconnected(final BluetoothGatt gatt,
                                     final BluetoothDevice device)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Writing to cap was finished successfully!", Toast.LENGTH_LONG).show();
            }

            //after write, we need to move on to the next value
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "device has been disconnected");

            }
        });
    }

    @Override
    public void uiDeviceFound(BluetoothDevice device, int rssi, byte[] record) {
        // no need to handle that in this Activity (here, we are not scanning)
    }

    //triggered when rssi is a different value?
    public void uiNewRssiAvailable(final BluetoothGatt gatt,
                                   final BluetoothDevice device,
                                   final int rssi)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    //
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


    //called when a new characteristic is finished reading. this should have our json object to parse through
    public void uiNewValueForCharacteristic(final BluetoothGatt gatt,
                                            final BluetoothDevice device,
                                            final BluetoothGattService service,
                                            final BluetoothGattCharacteristic characteristic,
                                            final String strValue,
                                            final int intValue,
                                            final byte[] rawValue,
                                            final String timestamp)
    {
        Log.d(TAG, "newvalueforchar");
        Log.d(TAG, strValue);

        //get all the keys and values and make sure they match up with the patient model

        //decode json object
        mBleWrapper.diconnect();
    }


    //this should be called after writedatatocharacteristic
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