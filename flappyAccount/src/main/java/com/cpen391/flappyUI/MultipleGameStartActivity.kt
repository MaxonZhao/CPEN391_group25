package com.cpen391.flappyUI

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.activity.StartActivity
import com.cpen391.flappyaccount.databinding.ActivityMultipleGameStartBinding

class MultipleGameStartActivity : MvvmActivity<ActivityMultipleGameStartBinding>(){
    private val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()
    }

    override fun initView() {
        super.initView()
        binding.apply {
            startYes.setOnClickListener {
                startActivity(Intent(context, StartActivity::class.java))
            }
            startNo.setOnClickListener {
                startActivity(Intent(context, SelectGameModeActivity::class.java))
            }
            profileIcon.setOnClickListener{
                startActivity(Intent(context, PersonalDataActivity::class.java))
            }
        }
    }

    override fun initObserver() {
    }

    override fun bind(): ActivityMultipleGameStartBinding {
        return ActivityMultipleGameStartBinding.inflate(layoutInflater)
    }

}