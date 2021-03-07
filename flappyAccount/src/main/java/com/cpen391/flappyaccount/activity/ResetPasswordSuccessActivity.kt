package com.cpen391.flappyaccount.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.R
import com.cpen391.flappyaccount.databinding.ActivityForgetPasswordBinding
import com.cpen391.flappyaccount.databinding.ActivityResetPasswordSuccessBinding

class ResetPasswordSuccessActivity : MvvmActivity<ActivityResetPasswordSuccessBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()

    }

    override fun initView() {
        super.initView()
        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    override fun initObserver() {

    }

    override fun bind(): ActivityResetPasswordSuccessBinding {
        return ActivityResetPasswordSuccessBinding.inflate(layoutInflater)
    }
}