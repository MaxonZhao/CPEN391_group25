package com.cpen391.flappyaccount.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.databinding.ActivityForgetPasswordBinding

class ForgetPasswordActivity : MvvmActivity<ActivityForgetPasswordBinding>() {
    override fun initObserver() {

    }

    override fun bind(): ActivityForgetPasswordBinding {
        return ActivityForgetPasswordBinding.inflate(layoutInflater)
    }

}