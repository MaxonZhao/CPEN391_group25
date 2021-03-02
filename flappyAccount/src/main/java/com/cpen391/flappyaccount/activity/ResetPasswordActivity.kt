package com.cpen391.flappyaccount.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.databinding.ActivityResetPasswordBinding

class ResetPasswordActivity : MvvmActivity<ActivityResetPasswordBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()

        binding.resetBtn.setOnClickListener {
            startActivity(Intent(this, ResetPasswordSuccessActivity::class.java))
        }
    }

    override fun initObserver() {

    }

    override fun bind(): ActivityResetPasswordBinding {
        return ActivityResetPasswordBinding.inflate(layoutInflater)
    }
}