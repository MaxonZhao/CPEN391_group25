package com.cpen391.flappyaccount.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.R
import com.cpen391.flappyaccount.databinding.ActivityVerifyOTPBinding

class VerifyOTPActivity : MvvmActivity<ActivityVerifyOTPBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()

        binding.verifyBtn.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }
    }

    override fun initObserver() {

    }

    override fun bind(): ActivityVerifyOTPBinding {
        return ActivityVerifyOTPBinding.inflate(layoutInflater)
    }
}