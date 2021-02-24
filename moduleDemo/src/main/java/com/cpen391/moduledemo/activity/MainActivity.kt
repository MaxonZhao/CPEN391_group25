package com.cpen391.moduledemo.activity

import android.os.Bundle
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.moduledemo.R
import com.cpen391.moduledemo.databinding.ActivityMainBinding

class MainActivity : MvvmActivity<ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun initObserver() {

    }

    override fun bind(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }
}