package com.cpen391.flappyaccount.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.databinding.ActivitySignupBinding
import com.cpen391.flappyaccount.model.bean.User
import com.cpen391.flappyaccount.viewmodel.SignUpViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import timber.log.Timber


class SignUpActivity : MvvmActivity<ActivitySignupBinding>() {

    private val signUpViewModel by viewModels<SignUpViewModel>()
    private val mDatabase = FirebaseDatabase.getInstance()
    private val mUserRef: DatabaseReference = mDatabase.getReference("User")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()

        binding.registerButton.setOnClickListener{
            registerUser()
        }



        Timber.d(mUserRef.toString() + " hello there")


    }
    override fun initObserver() {

    }

    fun registerUser() {
        val isValidated: Boolean =
            validateFullName()    &&
            validateUserName()    &&
            validateEmail()       &&
            validatePhoneNumber() &&
                    validatePassword()

        if (isValidated) {
            val fullName = binding.fullName.editText!!.text.toString()
            val username = binding.username.editText!!.text.toString()
            val email = binding.email.editText!!.text.toString()
            val password = binding.password.editText!!.text.toString()
            val countryCode = binding.countryCode.fullNumber
            val phoneNo = "+" + countryCode + binding.phoneNo.editText!!.text.toString()
            val user: User = User(
                fullName,
                username,
                email,
                phoneNo,
                password
            )
            mUserRef.child(user.userName).setValue(user).addOnSuccessListener {
                Toast.makeText(this, "You are registered!", Toast.LENGTH_LONG).show()
                finish()
            }
        }
        else Timber.d("failed to register this user")
    }
    fun validateFullName(): Boolean {
       val name: String = binding.fullName.editText?.text.toString()

       if (name == null || name.isEmpty()) {
           binding.fullName?.error = "Field cannot be Empty"
           return false
       } else {
           binding.fullName?.error = null
           binding.fullName?.isErrorEnabled = false
           return true
       }
    }

    fun validateUserName(): Boolean {
        val name: String = binding.username.editText?.text.toString()
        val noWhitSpace: String = "^([a-zA-Z0-9!@#\$%^&*()-_=+;:'\"|~`<>?/{}]{1,16})\$"
        val regex: Regex = Regex(noWhitSpace)

        if (name == null || name.isEmpty()) {
            binding.username?.error = "Field cannot be Empty"
            return false
        } else if (name.length >= 15) {
            binding.username?.error = "Username too long"
            return false
        } else if (!name.matches(regex)) {
            binding.username?.error = "White Spaces are not allowed"
            return false
        } else {
            binding.username?.error = null
            binding.username?.isErrorEnabled = false
            return true
        }
    }

    fun validateEmail(): Boolean {
        val emailAddr: String = binding.email.editText?.text.toString()
        val emailPattern: String = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val regex: Regex = Regex(emailPattern)

        if (emailAddr == null || emailAddr.isEmpty()) {
            binding.email?.error = "Field cannot be Empty"
            return false
        } else if (!emailAddr.matches(regex)) {
            binding.email?.error = "Invalid email Address"
            return false
        } else {
            binding.email?.error = null
            return true
        }
    }

    fun validatePhoneNumber(): Boolean {
        // todo: validate phone number according to country code
        val phoneNo: String = binding.phoneNo.editText?.text.toString()

        if (phoneNo == null || phoneNo.isEmpty()) {
            binding.phoneNo?.error = "Field cannot be Empty"
            return false
        } else {
            binding.phoneNo?.error = null
            return true
        }
    }

    fun validatePassword(): Boolean {
        val password: String = binding.password.editText?.text.toString()
        val passwordVal: String = "^" +
//            "(?=.*[0-9])" +             // at least 1 digit
//            "(?=.*[a-z])" +             // at least 1 lower case letter
//            "(?=.*[A-Z])" +             // at least 1 upper case letter
            "[a-zA-Z]{4,}" +                                // any 4 or more letter
            "(?=.*[!@#\$%^&+=]).*"      +                                     // at least 1 special character
            "[a-zA-Z0-9!@#\$%^&*()-_=+;:'\"|~`<>?/{}]*"   +                   // no white space
            "$"

        val regex: Regex = Regex(passwordVal)


        if (password == null || password.isEmpty()) {
            binding.password?.error = "Field cannot be Empty"
            return false
        } else if (!password.matches(regex)) {
            binding.password?.error = "Password is too weak"
            return false
        } else {
            binding.password?.error = null
            binding.password?.isErrorEnabled = false
            return true
        }
    }

    override fun bind(): ActivitySignupBinding {
        return ActivitySignupBinding.inflate(layoutInflater)
    }
}