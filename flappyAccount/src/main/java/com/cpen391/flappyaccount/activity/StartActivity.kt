package com.cpen391.flappyaccount.activity

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.ActionBar
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.databinding.ActivityStartBinding
import com.cpen391.flappyaccount.model.bean.User
import timber.log.Timber

class StartActivity : MvvmActivity<ActivityStartBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()
    }

    override fun initObserver() {

    }

    override fun bind(): ActivityStartBinding {
        return ActivityStartBinding.inflate(layoutInflater)
    }
}