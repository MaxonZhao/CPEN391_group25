package com.cpen391.flappybluetooth.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappybluetooth.R
import com.cpen391.flappybluetooth.databinding.ActivityHomeBinding
import timber.log.Timber


class HomeActivity : MvvmActivity<ActivityHomeBinding>() {

    private lateinit var bAdapter: BluetoothAdapter
    private lateinit var pairedListAdapter: ArrayAdapter<String>
    private lateinit var availableListAdapter: ArrayAdapter<String>

    private val paired_devices_list = ArrayList<String>()
    private val available_devices_list = ArrayList<String>()
    private val REQUEST_CODE_ENABLE_BT: Int = 1
    private val REQUEST_CODE_DISCOVERABLE_BT: Int = 2
    private val DISCOVERABLE_DURATION_TIME: Int = 300

    // Create a BroadcastReceiver for ACTION_FOUND.
    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action: String = intent.action
            when(action) {
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    Timber.d(" -------------  started!")
                }

                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    Timber.d("------------ finished!")
                }

                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address // MAC address
                    Timber.d("found ---: $device\n")
                    available_devices_list.add("$deviceName")
                    availableListAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()


        availableListAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            available_devices_list
        )
        pairedListAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            paired_devices_list
        )
        binding.availableDevicesView.adapter = availableListAdapter
        binding.pairedDevicesView.adapter = pairedListAdapter

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        bAdapter = BluetoothAdapter.getDefaultAdapter()

        if (bAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show()
        } else {
//                if (!bAdapter.isEnabled) {
//                    val enableIntent: Intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//                    startActivityForResult(enableIntent, REQUEST_CODE_ENABLE_BT)
//
//                } else {
//                    if (bAdapter.isDiscovering) bAdapter.cancelDiscovery()
//                    bAdapter.startDiscovery()
//                }

                if (!bAdapter.isDiscovering) {
                    val discoverableIntent: Intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
                    discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_DURATION_TIME)
                    startActivityForResult(discoverableIntent, REQUEST_CODE_DISCOVERABLE_BT)
                } else {
                    bAdapter.cancelDiscovery()
                    bAdapter.startDiscovery()
                }
        }
    }





    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {

            REQUEST_CODE_DISCOVERABLE_BT -> {
                if (resultCode == 120) {
                    binding.bluetoothIcon.setImageResource(R.drawable.bluetooth_on_64)
                    Toast.makeText(this, "Your device is discoverable now!", Toast.LENGTH_LONG)
                        .show()


                    val pairedDevices: Set<BluetoothDevice>? = bAdapter?.bondedDevices
                    pairedDevices?.forEach { device ->
                        val deviceName = device.name
                        val deviceHardwareAddress = device.address // MAC address
                        paired_devices_list.add("$deviceName")
                    }

                    pairedListAdapter.notifyDataSetChanged()
                    if (bAdapter.isDiscovering) {
                        bAdapter.cancelDiscovery()
                    }
                    bAdapter.startDiscovery()
                    available_devices_list.add("HUAWEI P20 Pro")
                    available_devices_list.add("YDPen")
                    availableListAdapter.notifyDataSetChanged()


                } else {
                    Toast.makeText(
                        this,
                        "You declined to make your device discoverable",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            REQUEST_CODE_ENABLE_BT -> {
                if (resultCode == Activity.RESULT_OK) {
                    binding.bluetoothIcon.setImageResource(R.drawable.bluetooth_on_64)
                    Toast.makeText(this, "Your device is enabled now!", Toast.LENGTH_LONG).show()
                    val pairedDevices: Set<BluetoothDevice>? = bAdapter?.bondedDevices
                    pairedDevices?.forEach { device ->
                        val deviceName = device.name
                        val deviceHardwareAddress = device.address // MAC address
                        paired_devices_list.add("$deviceName")
                    }

                    pairedListAdapter.notifyDataSetChanged()
                    if (bAdapter.isDiscovering) bAdapter.cancelDiscovery()
                    bAdapter.startDiscovery()

                } else {
                    Toast.makeText(this, "You declined to enable your device", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }



    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }
    override fun initObserver() {
    }

    override fun bind(): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(layoutInflater)
    }
}