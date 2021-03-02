package com.cpen391.flappyaccount.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.databinding.ActivityForgetPasswordBinding

class ForgetPasswordActivity : MvvmActivity<ActivityForgetPasswordBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()

        binding.next.setOnClickListener {
            startActivity(Intent(this, VerifyOTPActivity::class.java))
        }
    }

    override fun initObserver() {

    }

    override fun bind(): ActivityForgetPasswordBinding {
        return ActivityForgetPasswordBinding.inflate(layoutInflater)
    }

}