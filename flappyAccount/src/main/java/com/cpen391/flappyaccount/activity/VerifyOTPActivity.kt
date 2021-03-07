package com.cpen391.flappyaccount.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.databinding.ActivityVerifyOTPBinding
import com.cpen391.flappyaccount.model.bean.User
import com.cpen391.flappyaccount.viewmodel.VerifyOTPViewModel
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import timber.log.Timber
import java.util.concurrent.TimeUnit

class VerifyOTPActivity : MvvmActivity<ActivityVerifyOTPBinding>() {

    private val verifyOTPViewModel by viewModels<VerifyOTPViewModel>()
    private lateinit var userFound: User
    var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null


    private lateinit var task: Task<Void>

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
            if (pinCode == "") Toast.makeText(this, "Please enter the SMS verification Code", Toast.LENGTH_LONG)
            else verifyOTPViewModel.signInWithPhoneAuthCredential(this, pinCode)
        }
    }


    private fun startReset() {
        startActivity(Intent(this, ResetPasswordActivity::class.java))
    }

    override fun initObserver() {
        val owner = this
        verifyOTPViewModel.apply {
            verificationCompleted.observe(owner, {
                ResetPasswordActivity.actionStart(owner, userFound)
//                startActivity(Intent(owner, ResetPasswordActivity::class.java))
            })
        }
    }

    override fun initView() {
        super.initView()
        binding.hintTxt.text = "Enter one time code sent on ${userFound.phoneNo}"
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