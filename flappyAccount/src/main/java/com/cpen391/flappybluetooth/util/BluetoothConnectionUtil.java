package com.cpen391.flappybluetooth.util;


import android.content.Context;

import com.cpen391.flappybluetooth.activity.BluetoothConnectionService;

import java.nio.charset.Charset;

public class BluetoothConnectionUtil {

    public static int current_score = 0;
    private BluetoothConnectionService mBluetoothConnection = null;
    private static BluetoothConnectionUtil instance;

    private BluetoothConnectionUtil() {
    }



    public BluetoothConnectionService getBluetoothConnection() {
        return mBluetoothConnection;
    }

    public void setBluetoothConnection(BluetoothConnectionService mBluetoothConnection) {
        this.mBluetoothConnection = mBluetoothConnection;
    }

    public void sendSettingInfo(Context context, String msg1, String msg2, String msg3, String msg4) {
        byte[] bytes = new byte[4];
        bytes[0] = msg1.getBytes()[0];
        bytes[1] = msg2.getBytes()[0];
        bytes[2] = msg3.getBytes()[0];
        bytes[3] = msg4.getBytes()[0];
        if (mBluetoothConnection != null) mBluetoothConnection.write(bytes);

    }

    public void sendMessage(Context context, String message) {
        byte[] bytes = message.getBytes(Charset.defaultCharset());
        if (mBluetoothConnection != null) mBluetoothConnection.write(bytes);
    }


    public static BluetoothConnectionUtil getInstance() {
        if (instance == null) {
            //synchronized block to remove overhead
            synchronized (BluetoothConnectionUtil.class) {
                if (instance == null) {
                    // if instance is null, initialize
                    instance = new BluetoothConnectionUtil();
                }

            }
        }
        return instance;
    }


}