package com.cpen391.flappyUI

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.databinding.ActivityPersonalDataBinding

class PersonalDataActivity  : MvvmActivity<ActivityPersonalDataBinding>() {
    private val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()
        binding.userName.text = "a"
        binding.userEmail.text = "b"
        binding.top1Score.text = "b"
        binding.top2Score.text = "b"
        binding.top3Score.text = "b"
    }

    override fun initObserver() {
    }

    override fun bind(): ActivityPersonalDataBinding {
        return ActivityPersonalDataBinding.inflate(layoutInflater)
    }

}