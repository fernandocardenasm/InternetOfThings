package com.example.usuario.internetofthings;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ListActivity {

    // Initializes Bluetooth adapter.

    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    private LeDeviceListAdapter mLeDeviceListAdapter;

    private Button mButtonScan;
    private Button mButtonStop;
    private ProgressBar mProgressBar;
    private TextView mTextView;


    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 30000;

    ArrayList<BluetoothDevice> mLeDevices;
    private BTLE[] mBTLEDevices;
    int numDevices = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        mLeDevices = new ArrayList<BluetoothDevice>();
        mHandler = new Handler();

        mButtonScan = (Button) findViewById(R.id.scanButton);
        mButtonStop = (Button) findViewById(R.id.stopButton);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mTextView = (TextView) findViewById(R.id.textView);

        mProgressBar.setVisibility(View.INVISIBLE);
        mButtonStop.setEnabled(false);


        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

//        mBTLEDevices = new BTLE[numDevices];
//
//        for(int i = 0; i < numDevices; i++){
//            BTLE device = new BTLE();
//            device.setFriendlyName("AAA");
//            device.setDeviceAddress("MAC100");
//            mBTLEDevices[i] = device;
//        }
//
//        BTLEAdapter btleAdapter = new BTLEAdapter(this, mBTLEDevices);
//        setListAdapter(btleAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        // Initializes list view adapter.

        mLeDeviceListAdapter = new LeDeviceListAdapter(this, mLeDevices);
        setListAdapter(mLeDeviceListAdapter);

        mButtonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonScan.setEnabled(false);
                mButtonStop.setEnabled(true);
                mProgressBar.setVisibility(View.VISIBLE);
                mTextView.setVisibility(View.VISIBLE);
                mTextView.setText("Loading...");
                clear();
                scanLeDevice(true);
            }
        });
        mButtonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanLeDevice(false);
                mButtonStop.setEnabled(false);
                mButtonScan.setEnabled(true);
                mTextView.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
                scanLeDevice(false);
            }
        });
        //scanLeDevice(true);
        //Toast.makeText(this, "Probando", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
        clear();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        final BluetoothDevice device = getDevice(position);
        if (device == null) return;
        final Intent intent = new Intent(this, DeviceControlActivity.class);
        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.getName());
        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
        if (mScanning) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            mScanning = false;
        }

        startActivity(intent);
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    //invalidateOptionsMenu();
                    mButtonScan.setEnabled(true);
                    mButtonStop.setEnabled(false);
                    mTextView.setVisibility(View.INVISIBLE);
                    mProgressBar.setVisibility(View.INVISIBLE);

                    if (mLeDevices.size()==0){
                        mTextView.setVisibility(View.VISIBLE);
                        mTextView.setText(R.string.empty_list_text);
                    }
                }
            }, SCAN_PERIOD);
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            if (mLeDevices.size()==0){
                mTextView.setVisibility(View.VISIBLE);
                mTextView.setText(R.string.empty_list_text);
            }
        }
        //invalidateOptionsMenu();
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addDevice(device);
                            mLeDeviceListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            };

    public void addDevice(BluetoothDevice device) {
        if(!mLeDevices.contains(device)) {
            mLeDevices.add(device);
            //Toast.makeText(MainActivity.this, "Add", Toast.LENGTH_SHORT).show();
        }
    }
    public BluetoothDevice getDevice(int position) {
        return mLeDevices.get(position);
    }
    public void clear() {
        mLeDevices.clear();
    }

}
