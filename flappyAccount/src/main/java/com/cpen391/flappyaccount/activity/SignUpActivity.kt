package com.cpen391.flappyaccount.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.databinding.ActivitySignupBinding

class SignUpActivity : MvvmActivity<ActivitySignupBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()
    }
    override fun initObserver() {

    }

    override fun bind(): ActivitySignupBinding {
        return ActivitySignupBinding.inflate(layoutInflater)
    }
}