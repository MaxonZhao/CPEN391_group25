package com.cpen391.flappyaccount.activity

import android.os.Bundle
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.R

import com.cpen391.flappyaccount.databinding.ActivityLoginBinding

class LoginActivity : MvvmActivity<ActivityLoginBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun initObserver() {
    }

    override fun bind(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }
}