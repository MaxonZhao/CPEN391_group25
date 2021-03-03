package com.cpen391.flappyaccount.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.databinding.ActivityLoginBinding
import com.cpen391.flappyaccount.model.bean.User
import com.cpen391.flappyaccount.viewmodel.LoginViewModel
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import timber.log.Timber

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
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
        }

        binding.loginBtn.setOnClickListener {
            loginUser()
        }

        binding.guestBtn.setOnClickListener {
            startActivity(Intent(this, StartActivity::class.java))
        }
    }

    override fun initObserver() {
    }

    fun loginUser() {
//        val isValid: Boolean = true
        if (!validateUserName() || !validatePassword()) {
            return
        } else {
            doLogin()
        }
    }

    fun doLogin() {
        val userEnteredUserName = binding.username.editText!!.text.toString().trim()
        val userEnteredPassword = binding.password.editText!!.text.toString().trim()

        val checkUser: Query = mUserRef.orderByChild("userName").equalTo(userEnteredUserName)
        Timber.d(checkUser.toString() + " emmm")


        checkUser.addListenerForSingleValueEvent (object:ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    Timber.d("hello")
//                    val user = snapshot.child(userEnteredUserName).value
//                    Timber.d(snapshot.child(userEnteredUserName).value)
                    val passwordFromDB = snapshot.child(userEnteredUserName).child("password").value

                    Timber.d("fetched password is: " + passwordFromDB)
                    if (passwordFromDB!!.equals(userEnteredPassword)) {
                        Timber.d("login success")
                        Toast.makeText(applicationContext, "Login successful", Toast.LENGTH_SHORT).show()
                        startStartActivity()
                    } else {
                        binding.password.setError("Wrong Password")
                        binding.password.requestFocus()
                    }
                } else {
                    binding.username.setError("No such user")
                    binding.username.requestFocus()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.w(error.toException())
            }

        })

    }
    fun startStartActivity() {
        startActivity(Intent(this, StartActivity::class.java))
    }

    fun validateUserName(): Boolean {
        val name: String = binding.username.editText?.text.toString()

        if (name == null || name.isEmpty()) {
            binding.username?.error = "Field cannot be Empty"
            return false
        } else {
            binding.username?.error = null
            binding.username?.isErrorEnabled = false
            return true
        }
    }

    fun validatePassword(): Boolean {
        val password: String = binding.password.editText?.text.toString()


        if (password == null || password.isEmpty()) {
            binding.password?.error = "Field cannot be Empty"
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