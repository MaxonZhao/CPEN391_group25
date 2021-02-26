package com.cpen391.flappyaccount.activity

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.R

import com.cpen391.flappyaccount.databinding.ActivityLoginBinding
import timber.log.Timber

class LoginActivity : MvvmActivity<ActivityLoginBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()
    }

    override fun initObserver() {
    }

    override fun bind(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }
}