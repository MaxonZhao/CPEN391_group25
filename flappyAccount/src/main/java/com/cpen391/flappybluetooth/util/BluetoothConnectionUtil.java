package com.cpen391.flappybluetooth.util;


import android.content.Context;
import android.widget.Toast;

import com.cpen391.flappybluetooth.activity.BluetoothConnectionService;
import com.cpen391.flappybluetooth.activity.MainActivity;

import java.nio.charset.Charset;

// Java code to explain double check locking
public class BluetoothConnectionUtil
{
    // private instance, so that it can be
    // accessed by only by getInstance() method
    public volatile static boolean readyToSend = true;
    public volatile static boolean readyToStart = true;
    private BluetoothConnectionService mBluetoothConnection = null;
    private static BluetoothConnectionUtil instance;
    private BluetoothConnectionUtil()
    {
        // private constructor
    }

    public BluetoothConnectionService getBluetoothConnection() {
        return mBluetoothConnection;
    }

    public void setBluetoothConnection(BluetoothConnectionService mBluetoothConnection) {
        this.mBluetoothConnection = mBluetoothConnection;
    }


    // TODO: define parameter list as needed
    public void sendSettingInfo(Context context, String msg1, String msg2, String msg3, String msg4) {
        if (BluetoothConnectionUtil.readyToSend) {
            byte[] bytes = new byte[4];
            bytes[0] = msg1.getBytes()[0];
            bytes[1] = msg2.getBytes()[0];
            bytes[2] = msg3.getBytes()[0];
            bytes[3] = msg4.getBytes()[0];
            if (mBluetoothConnection != null) mBluetoothConnection.write(bytes);
        } else {
            Toast.makeText(context, "Gaming station is not set up yet!", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendMessage(Context context, String message) {
        if (BluetoothConnectionUtil.readyToStart) {
            byte[] bytes = message.getBytes(Charset.defaultCharset());
            if (mBluetoothConnection != null) mBluetoothConnection.write(bytes);
        } else {
            Toast.makeText(context, "Gaming station did not receive user info yet!", Toast.LENGTH_SHORT).show();
        }
    }


    public static BluetoothConnectionUtil getInstance()
    {
        if (instance == null)
        {
            //synchronized block to remove overhead
            synchronized (BluetoothConnectionUtil.class)
            {
                if(instance==null)
                {
                    // if instance is null, initialize
                    instance = new BluetoothConnectionUtil();
                }

            }
        }
        return instance;
    }


}