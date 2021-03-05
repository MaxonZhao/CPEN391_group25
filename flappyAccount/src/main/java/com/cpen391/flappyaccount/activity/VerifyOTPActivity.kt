package com.cpen391.flappyaccount.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.databinding.ActivityVerifyOTPBinding
import com.cpen391.flappyaccount.model.bean.User
import com.cpen391.flappyaccount.viewmodel.VerifyOTPViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import timber.log.Timber
import java.util.concurrent.TimeUnit

class VerifyOTPActivity : MvvmActivity<ActivityVerifyOTPBinding>() {

    private val verifyOTPViewModel by viewModels<VerifyOTPViewModel>()
    private lateinit var userFound: User
    val TIME_OUT = 60
    var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        userFound = intent.getSerializableExtra("User") as User
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()


        verifyOTPViewModel.init(this)

        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                Timber.d("SMS code is ${p0.smsCode}")
                Toast.makeText(applicationContext, "Verification Complete", Toast.LENGTH_SHORT).show()
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(applicationContext, "Verification Failed: please register with phone number in a correct format", Toast.LENGTH_LONG*2).show()
                Timber.d(p0)
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)

                val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(p0, "aaa")
                Timber.d("??????????? ${credential.smsCode}")
                Toast.makeText(applicationContext, "verification code is $p0", Toast.LENGTH_LONG)
            }
        }

        binding.verifyBtn.setOnClickListener {
            sendVerificationCodeToUser(userFound.phoneNo)
//            verifyOTPViewModel.sendVerificationCodeToUser("+8615618219971", applicationContext)
//            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }
    }

    private fun sendVerificationCodeToUser(phoneNo: String) {
        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(phoneNo)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(mCallback)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun initObserver() {

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
    }
}