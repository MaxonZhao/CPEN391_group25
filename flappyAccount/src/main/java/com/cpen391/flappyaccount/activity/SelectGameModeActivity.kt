package com.cpen391.flappyaccount.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.databinding.ActivitySelectGameModeBinding

class SelectGameModeActivity : MvvmActivity<ActivitySelectGameModeBinding>(){

    private val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()
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