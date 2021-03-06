package com.cpen391.flappyaccount.viewmodel

import android.app.Activity
import android.content.Context

import androidx.lifecycle.MutableLiveData
import com.cpen391.appbase.ui.mvvm.BaseViewModel

import com.cpen391.flappyaccount.activity.VerifyOTPActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 *  VerifyOTPViewModel
 *  contains logic operations separated form view
 *
 *  @note: asynchronous call to find user from remote database, implemented using RxJava
 *
 *
 *  @autho Yuefeng Zhao
 */

class VerifyOTPViewModel : BaseViewModel() {

    val verificationCompleted: MutableLiveData<Boolean> = MutableLiveData()
    private val TIMEOUT = 60L
    private var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null

    fun init() {

        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                Timber.d("SMS code is ${p0.smsCode}")
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Timber.d(p0)
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                VerifyOTPActivity.verificationId = p0
            }
        }
    }

    fun sendVerificationCodeToUser(phoneNo: String, context: Context) {
        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(phoneNo)                    // Phone number to verify
            .setTimeout(TIMEOUT, TimeUnit.SECONDS)      // Timeout and unit
            .setActivity(context as Activity)           // Activity (for callback binding)
            .setCallbacks(mCallback)                    // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun signInWithPhoneAuthCredential(context: Context, code: String) {
        val credential: PhoneAuthCredential =
            PhoneAuthProvider.getCredential(VerifyOTPActivity.verificationId, code)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(context as Activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d("signInWithCredential:success")

                    verificationCompleted.value = true
                } else {
                    // Sign in failed, display a message and update the UI
                    Timber.d(task.exception)
                    verificationCompleted.value = false
                }
            }
    }
}