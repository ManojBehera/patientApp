package com.example.imac.chs_pharmacy;

import static java.util.UUID.fromString;

import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imac.chs_pharmacy.BleNamesResolver;
import com.example.imac.chs_pharmacy.BleWrapper;
import com.example.imac.chs_pharmacy.BleWrapperUiCallbacks;


public class MainActivity extends Activity
{
    private final String LOGTAG = "BLETEST";
    private final String TARGET = "SensorTag";
    private BleWrapper mBleWrapper = null;
    private mSensorState mState;
    private String gattList = "";
    private TextView mTv;

    private enum mSensorState {IDLE, ACC_ENABLE, ACC_READ};

    public final static UUID

            UUID_ACC_SERV = fromString("0000180b-0000-1000-8000-00805f9b34fb"),
            UUID_ACC_DATA = fromString("00003001-0000-1000-8000-00805f9b34fb"),
            UUID_ACC_CONF = fromString("00003002-0000-1000-8000-00805f9b34fb");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBleWrapper = new BleWrapper(this, new BleWrapperUiCallbacks.Null()
        {
            @Override
            public void uiDeviceFound(final BluetoothDevice device, final int rssi, final byte[] record)
            {
                Log.d(LOGTAG, "uiDeviceFound: "+device.getName()+", "+rssi+", "+record.toString());
                if (device.getName().equals(TARGET))
                {
                    if (!mBleWrapper.connect(device.getAddress()))
                    {
                        Log.d(LOGTAG, "uiDeviceFound: Problem connecting to remote device.");
                    }
                }
            }

            @Override
            public void uiDeviceConnected(BluetoothGatt gatt, BluetoothDevice device)
            {
                Log.d(LOGTAG, "uiDeviceConnected: State = " + mBleWrapper.getAdapter().getState());
            }

            @Override
            public void uiDeviceDisconnected(BluetoothGatt gatt, BluetoothDevice device) {
                Log.d(LOGTAG, "uiDeviceDisconnected: State = " + mBleWrapper.getAdapter().getState());
                gatt.disconnect();
            }

            @Override
            public void uiAvailableServices(BluetoothGatt gatt, BluetoothDevice device, List<BluetoothGattService> services)
            {
                BluetoothGattCharacteristic c;
                BluetoothGattDescriptor d;

                for (BluetoothGattService service : services)
                {
                    String serviceName = BleNamesResolver.resolveUuid(service.getUuid().toString());
                    Log.d(LOGTAG, serviceName);
                    gattList += serviceName + "\n";

                    mBleWrapper.getCharacteristicsForService(service);
                }

                // enable services
                Log.d(LOGTAG, "uiAvailableServices: Enabling services");
                c = gatt.getService(UUID_ACC_SERV).getCharacteristic(UUID_ACC_CONF);
                mBleWrapper.writeDataToCharacteristic(c, new byte[] {0x01});
                mState = mSensorState.ACC_ENABLE;



                // set notification on characteristic
                //Log.d(LOGTAG, "uiAvailableServices: Setting notification");
                //c = gatt.getService(UUID_IRT_SERV).getCharacteristic(UUID_IRT_DATA);
                //mBleWrapper.setNotificationForCharacteristic(c, true);

                // enable notification on descriptor
                //d = c.getDescriptor(UUID_CCC_DESC);
                //d.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                //gatt.writeDescriptor(d);
            }

            @Override
            public void uiCharacteristicForService(	BluetoothGatt gatt,
                                                       BluetoothDevice device,
                                                       BluetoothGattService service,
                                                       List<BluetoothGattCharacteristic> chars)
            {
                super.uiCharacteristicForService(gatt, device, service, chars);
                for (BluetoothGattCharacteristic c : chars)
                {
                    String charName = BleNamesResolver.resolveCharacteristicName(c.getUuid().toString());
                    Log.d(LOGTAG, charName);
                    gattList += "Characteristic: " + charName + "\n";
                }
            }


            @Override
            public void uiSuccessfulWrite(	BluetoothGatt gatt,
                                              BluetoothDevice device,
                                              BluetoothGattService service,
                                              BluetoothGattCharacteristic ch,
                                              String description)
            {
                BluetoothGattCharacteristic c;

                super.uiSuccessfulWrite(gatt, device, service, ch, description);
                Log.d(LOGTAG, "uiSuccessfulWrite");

                switch (mState)
                {
                    case ACC_ENABLE:
                        Log.d(LOGTAG, "uiSuccessfulWrite: Reading acc");
                        c = gatt.getService(UUID_ACC_SERV).getCharacteristic(UUID_ACC_DATA);
                        mBleWrapper.requestCharacteristicValue(c);
                        mState = mSensorState.ACC_READ;
                        break;

                    case ACC_READ:
                        Log.d(LOGTAG, "uiSuccessfulWrite: state = ACC_READ");
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void uiFailedWrite(	BluetoothGatt gatt,
                                          BluetoothDevice device,
                                          BluetoothGattService service,
                                          BluetoothGattCharacteristic ch,
                                          String description)
            {
                super.uiFailedWrite(gatt, device, service, ch, description);
                Log.d(LOGTAG, "uiFailedWrite");
            }

            @Override
            public void uiNewValueForCharacteristic(BluetoothGatt gatt,
                                                    BluetoothDevice device,
                                                    BluetoothGattService service,
                                                    BluetoothGattCharacteristic ch,
                                                    String strValue,
                                                    int intValue,
                                                    byte[] rawValue,
                                                    String timestamp)
            {
                super.uiNewValueForCharacteristic(gatt, device, service, ch, strValue, intValue, rawValue, timestamp);
                Log.d(LOGTAG, "uiNewValueForCharacteristic");
                for (byte b:rawValue)
                {
                    Log.d(LOGTAG, "Val: " + b);
                }
            }

            @Override
            public void uiGotNotification(	BluetoothGatt gatt,
                                              BluetoothDevice device,
                                              BluetoothGattService service,
                                              BluetoothGattCharacteristic characteristic)
            {
                super.uiGotNotification(gatt, device, service, characteristic);
                String ch = BleNamesResolver.resolveCharacteristicName(characteristic.getUuid().toString());

                Log.d(LOGTAG,  "uiGotNotification: " + ch);
            }
        });

        // check for BLE
        if (mBleWrapper.checkBleHardwareAvailable() == false)
        {
            Toast.makeText(this, "BLE Hardware missing", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // check for Bluetooth enabled on each resume
        if (mBleWrapper.isBtEnabled() == false)
        {
            // BT not enabled. Request to turn it on. User needs to restart app once it's turned on.
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_scan:
                startScan();
                break;

            case R.id.action_stop:
                stopScan();
                break;

            case R.id.action_test:
                testButton();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // start the BLE scan
    public void startScan()
    {
        Log.d(LOGTAG, "startScan");
        mBleWrapper.startScanning();
    }

    // stop the BLE scan
    private void stopScan()
    {
        Log.d(LOGTAG, "stopSCan");
        mBleWrapper.stopScanning();
        Toast.makeText(this, "Scan finished", Toast.LENGTH_SHORT).show();
    }

    private void testButton()
    {
        BluetoothGatt gatt;
        BluetoothGattCharacteristic c;

        if (!mBleWrapper.isConnected()) {
            return;
        }

        //mTv = (TextView)findViewById(R.id.textView1);
        //mTv.setText(gattList);

        //Log.d(LOGTAG, "testButton: Reading acc");
        //gatt = mBleWrapper.getGatt();
        //c = gatt.getService(UUID_ACC_SERV).getCharacteristic(UUID_ACC_DATA);
        //mBleWrapper.requestCharacteristicValue(c);

        //Log.d(LOGTAG, "uiAvailableServices: Setting notification");
        gatt = mBleWrapper.getGatt();
        c = gatt.getService(UUID_ACC_SERV).getCharacteristic(UUID_ACC_DATA);
        mBleWrapper.setNotificationForCharacteristic(c, true);

        //c = gatt.getService(UUID_IRT_SERV).getCharacteristic(UUID_IRT_DATA);
        //mBleWrapper.requestCharacteristicValue(c);



        //		if (c.getValue()!= null) {
        //			Log.d(LOGTAG, "testButton: " + c.getValue()[0]);
        //		}
    }
}