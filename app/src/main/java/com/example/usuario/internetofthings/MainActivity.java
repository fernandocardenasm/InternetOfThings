package com.example.usuario.internetofthings;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ListActivity {

    // Initializes Bluetooth adapter.
//    final BluetoothManager bluetoothManager =
//            (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
//    private BluetoothAdapter mBluetoothAdapter;

    private BTLE[] mBTLEDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBTLEDevices[0].setDeviceAddress("MAC");
        mBTLEDevices[0].setFriendlyName("ALO");
        mBTLEDevices[1].setDeviceAddress("MAC100");
        mBTLEDevices[1].setFriendlyName("ALO100");

        BTLEAdapter btleAdapter = new BTLEAdapter(this, mBTLEDevices);
        setListAdapter(btleAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
