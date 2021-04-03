package com.cpen391.flappyaccount.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.observe
import com.cpen391.appbase.network.SimpleObserver
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.Injection
import com.cpen391.flappyaccount.consts.*
import com.cpen391.flappyaccount.databinding.ActivitySignupBinding
import com.cpen391.flappyaccount.model.bean.User
import com.cpen391.flappyaccount.viewmodel.SignUpViewModel
import com.google.firebase.database.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


class SignUpActivity : MvvmActivity<ActivitySignupBinding>() {

    private val signUpViewModel by viewModels<SignUpViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()
    }

    override fun initView() {
        super.initView()
        binding.registerButton.setOnClickListener{
            registerUser()
        }
    }

    override fun initObserver() {
        val owner = this
        signUpViewModel.apply {
            registerResult.observe(owner) {
                onRegisterResult(it)
            }

            fullNameHasError.observe(owner) {
                displayFullNameErrorState(it)
            }

            usernameHasError.observe(owner) {
                displayUsernameErrorState(it)
            }

            emailHasError.observe(owner) {
                displayEmailErrorState(it)
            }

            phoneNoHasError.observe(owner) {
                displayPhoneNoErrorState(it)
            }

            passwordHasError.observe(owner) {
                displayPasswordErrorState(it)
            }
        }
    }


    private fun onRegisterResult(success: Boolean) {
        if (success) {
            Toast.makeText(applicationContext, "You are registered!", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(applicationContext, "something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun registerUser() {
        val fullName = binding.fullName.editText!!.text.toString()
        val username = binding.username.editText!!.text.toString()
        val email = binding.email.editText!!.text.toString()
        val password = binding.password.editText!!.text.toString()
        val countryCode = binding.countryCode.fullNumber
        val phoneNo = binding.phoneNo.editText!!.text.toString()
        signUpViewModel.registerUser(fullName,  username, email, phoneNo, password, countryCode)
    }



    private fun displayFullNameErrorState(error: Boolean) {
        when (error) {
            true -> binding.fullName?.error = "Field cannot be Empty"
            false -> {
                binding.fullName?.error = null
                binding.fullName?.isErrorEnabled = false
            }
        }
    }

    private fun displayUsernameErrorState(error: String) {
        when (error) {
            USERNAME_EMPTY -> binding.username.error = "Field cannot be Empty"
            USERNAME_TOO_LONG -> binding.username.error = "Username too long"
            USERNAME_HAS_WHITE_SPACE -> binding.username.error = "Username has white space"
            USERNAME_VALID -> {
                binding.username?.error = null
                binding.username?.isErrorEnabled = false
            }
        }
    }

    private fun displayEmailErrorState(error: String) {
        when (error) {
            EMAIL_EMPTY -> binding.email.error = "Field cannot be Empty"
            EMAIL_INVALID -> binding.email.error = "Invalid email address"
            EMAIL_VALID -> {
                binding.email.error = null
                binding.email.isErrorEnabled = false
            }
        }
    }

    private fun displayPhoneNoErrorState(error: Boolean) {
        when (error) {
            true -> binding.phoneNo.error = "Field cannot be Empty"
            false -> {
                binding.phoneNo.error = null
                binding.phoneNo.isErrorEnabled = false
            }
        }
    }

    private fun displayPasswordErrorState(error: String) {
        when (error) {
            PASSWORD_EMPTY -> binding.password.error = "Field cannot be Empty"
            PASSWORD_TOO_WEAK -> binding.password.error = "password is too weak"
            PASSWORD_VALID -> {
                binding.password.error = null
                binding.password.isErrorEnabled = false
            }
        }
    }


    override fun bind(): ActivitySignupBinding {
        return ActivitySignupBinding.inflate(layoutInflater)
    }
}