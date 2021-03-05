package com.cpen391.flappyaccount.viewmodel

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.cpen391.appbase.ui.mvvm.BaseViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import timber.log.Timber
import java.util.concurrent.TimeUnit

class VerifyOTPViewModel : BaseViewModel() {

    private lateinit var auth: FirebaseAuth
    val TIME_OUT = 60
    var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null

    fun init(context: Context) {
        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                Timber.d("SMS code is ${p0.smsCode}")
                Toast.makeText(context, "Verification Complete", Toast.LENGTH_SHORT).show()
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(context, "Verification Failed: please register with phone number in a correct format", Toast.LENGTH_LONG*2).show()
                Timber.d(p0)
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                Toast.makeText(context, "verification code is $p0", Toast.LENGTH_LONG)
            }


        }
    }
    fun sendVerificationCodeToUser(phoneNo: String, context: Context) {
        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(phoneNo)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(context as Activity)                 // Activity (for callback binding)
            .setCallbacks(mCallback)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}