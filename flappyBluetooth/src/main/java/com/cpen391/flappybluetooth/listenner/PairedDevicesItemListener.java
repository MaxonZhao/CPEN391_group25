package com.cpen391.flappybluetooth.listenner;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.cpen391.flappybluetooth.activity.BluetoothConnectionService;
import com.cpen391.flappybluetooth.activity.MainActivity;

import timber.log.Timber;

public class PairedDevicesItemListener implements AdapterView.OnItemClickListener{
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //first cancel discovery because its very memory intensive.
//        MainActivity.mBluetoothAdapter.cancelDiscovery();
//
//        Log.d(TAG, "onItemClick: You Clicked on a device.");
//        String deviceName = mBTDevices.get(i).getName();
//        String deviceAddress = mBTDevices.get(i).getAddress();
//
//        Log.d(TAG, "onItemClick: deviceName = " + deviceName);
//        Log.d(TAG, "onItemClick: deviceAddress = " + deviceAddress);
//
//        //create the bond.
//        //NOTE: Requires API 17+? I think this is JellyBean
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
//            Log.d(TAG, "Trying to pair with " + deviceName);
//            Timber.d("------------------------" + mBTDevices.get(i).createBond());
//
//            mBTDevice = mBTDevices.get(i);
//            mBluetoothConnection = new BluetoothConnectionService(MainActivity.this);
//        }
    }
}
