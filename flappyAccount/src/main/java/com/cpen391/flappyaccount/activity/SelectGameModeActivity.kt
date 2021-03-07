package com.cpen391.flappyaccount.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.UserHandle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.databinding.ActivitySelectGameModeBinding
import com.cpen391.flappyaccount.model.bean.User
import timber.log.Timber

class SelectGameModeActivity : MvvmActivity<ActivitySelectGameModeBinding>(){

    private val context: Context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()
        val userName = UserInfoActivity.instance?.getUser()
        Timber.d("$userName")
    }

    override fun initView() {
        super.initView()
        binding.apply {
            singleMode.setOnClickListener {
                startActivity(Intent(context, SingleGameStartActivity::class.java))
            }
            mutipleMode.setOnClickListener {
                startActivity(Intent(context, MultipleGameStartActivity::class.java))
            }
            profileIcon.setOnClickListener{
                startActivity(Intent(context, PersonalDataActivity::class.java))
            }
        }
    }

    override fun initObserver() {
    }

    override fun bind(): ActivitySelectGameModeBinding {
        return ActivitySelectGameModeBinding.inflate(layoutInflater)
    }

}