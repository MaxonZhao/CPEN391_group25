package com.cpen391.flappyaccount.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.observe
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.databinding.ActivityVerifyOTPBinding
import com.cpen391.flappyaccount.model.bean.User
import com.cpen391.flappyaccount.viewmodel.VerifyOTPViewModel
import timber.log.Timber

class VerifyOTPActivity : MvvmActivity<ActivityVerifyOTPBinding>() {

    private val verifyOTPViewModel by viewModels<VerifyOTPViewModel>()
    private lateinit var userFound: User


    override fun onCreate(savedInstanceState: Bundle?) {
        userFound = intent.getSerializableExtra("User") as User
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()

        if (verificationId == null && savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        verifyOTPViewModel.init(this)
        verifyOTPViewModel.sendVerificationCodeToUser(userFound.phoneNo, this)

        binding.verifyBtn.setOnClickListener {
            val pinCode: String = binding.pin.text.toString()

            Timber.d("user input is $pinCode")
            if (pinCode == "") Toast.makeText(
                this,
                "Please enter the SMS verification Code",
                Toast.LENGTH_LONG
            )
            else verifyOTPViewModel.signInWithPhoneAuthCredential(this, pinCode)
        }
    }


    override fun initObserver() {
        val owner = this
        verifyOTPViewModel.apply {
            verificationCompleted.observe(owner) {
                ResetPasswordActivity.actionStart(owner, userFound)
            }
        }
    }

    override fun initView() {
        super.initView()
        binding.hintTxt.text = "Enter one time code sent on ${userFound.phoneNo}"
        binding.cancelBtn.setOnClickListener {
            finish()
        }
    }

    override fun bind(): ActivityVerifyOTPBinding {
        return ActivityVerifyOTPBinding.inflate(layoutInflater)
    }

    companion object {
        fun actionStart(context: Context, data1: User) {
            val intent = Intent(context, VerifyOTPActivity::class.java)
            intent.putExtra("User", data1)
            context.startActivity(intent)
        }

        var verificationId: String? = null
    }

}