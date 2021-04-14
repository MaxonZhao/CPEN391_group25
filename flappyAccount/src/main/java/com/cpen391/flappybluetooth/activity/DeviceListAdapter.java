package com.cpen391.flappybluetooth.activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cpen391.flappyaccount.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * DeviceListAdapter
 * an adapter for the list view in bluetooth UI page, it contains
 * an array of devices available/paired along with its name
 * @author Yuefeng Zhao
 */
public class DeviceListAdapter extends ArrayAdapter<BluetoothDevice> {

    private final LayoutInflater mLayoutInflater;
    private final ArrayList<BluetoothDevice> mDevices;
    private final int mViewResourceId;

    public DeviceListAdapter(Context context, int tvResourceId, ArrayList<BluetoothDevice> devices) {
        super(context, tvResourceId, devices);
        this.mDevices = devices;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = tvResourceId;
    }

    @SuppressLint("ViewHolder")
    public @NotNull View getView(int position, View convertView, @NotNull ViewGroup parent) {
        convertView = mLayoutInflater.inflate(mViewResourceId, null);

        BluetoothDevice device = mDevices.get(position);

        if (device != null) {
            TextView deviceName =  convertView.findViewById(R.id.tvDeviceName);
            TextView deviceAddress =  convertView.findViewById(R.id.tvDeviceAddress);

            if (deviceName != null) {
                deviceName.setText(device.getName());
            }
            if (deviceAddress != null) {
                deviceAddress.setText(device.getAddress());
            }
        }

        return convertView;
    }

}

