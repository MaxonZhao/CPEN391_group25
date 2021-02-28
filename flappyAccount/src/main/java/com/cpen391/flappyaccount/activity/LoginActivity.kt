package com.cpen391.flappyaccount.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.databinding.ActivityLoginBinding
import com.cpen391.flappyaccount.viewmodel.LoginViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class LoginActivity : MvvmActivity<ActivityLoginBinding>() {

    private val loginViewModel by viewModels<LoginViewModel>()
    private val mDatabase = FirebaseDatabase.getInstance()
    private val mUserRef: DatabaseReference = mDatabase.getReference("User")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()

        binding.signupBtn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.resetBtn.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }

        binding.loginBtn.setOnClickListener {

//            startActivity(Intent(this, StartActivity::class.java))
        }
    }

    override fun initObserver() {
    }

    fun loginUser() {
        if (!validateUserName() || !validatePassword()) {
            return
        } else {
            doLogin()
        }
    }

    fun doLogin() {
        val userEnteredUserName = binding.username.editText!!.text.toString().trim()
        val userEnteredPassword = binding.password.editText!!.text.toString().trim()

        val checkUser: Query = mUserRef.orderByChild("username").equalTo(userEnteredUserName)

//        checkUser.addListenerForSingleValueEvent (
//        })

    }

    fun validateUserName(): Boolean {
        val name: String = binding.username.editText?.text.toString()
        val noWhitSpace: String = "^([a-zA-Z0-9!@#\$%^&*()-_=+;:'\"|~`<>?/{}]{3,16})\$"
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

    override fun bind(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }
}