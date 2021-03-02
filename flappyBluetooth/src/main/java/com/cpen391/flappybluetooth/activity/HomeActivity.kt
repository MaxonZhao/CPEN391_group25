package com.cpen391.flappybluetooth.activity

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappybluetooth.databinding.ActivityHomeBinding


class HomeActivity : MvvmActivity<ActivityHomeBinding>() {

    lateinit var bAdapter: BluetoothAdapter

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()

        bAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show()

        } else {
            if (bAdapter.isEnabled)
                Toast.makeText(this, "Bluetooth is enabled", Toast.LENGTH_LONG).show()
            else
                Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show()
        }
    }

    override fun initObserver() {
    }

    override fun bind(): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(layoutInflater)
    }
}