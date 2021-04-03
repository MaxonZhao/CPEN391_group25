package com.cpen391.flappyaccount.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.observe
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyUI.SelectGameModeActivity
import com.cpen391.flappyaccount.consts.*
import com.cpen391.flappyaccount.databinding.ActivityLoginBinding
import com.cpen391.flappyaccount.model.api.LoggedInUser
import com.cpen391.flappyaccount.model.bean.User
import com.cpen391.flappyaccount.viewmodel.LoginViewModel
import com.cpen391.flappybluetooth.activity.MainActivity
import timber.log.Timber

class LoginActivity : MvvmActivity<ActivityLoginBinding>() {

    private val context: Context = this
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()
    }

    /*
      init buttons for directing pages
     */
    override fun initView() {
        super.initView()
        binding.apply {
            signupBtn.setOnClickListener {
                startActivity(Intent(context, SignUpActivity::class.java))
            }

            resetBtn.setOnClickListener {
                startActivity(Intent(context, ForgetPasswordActivity::class.java))
            }

            loginBtn.setOnClickListener {
                val userEnteredUserName = binding.username.editText!!.text.toString().trim()
                val userEnteredPassword = binding.password.editText!!.text.toString().trim()
                loginViewModel.login(userEnteredUserName, userEnteredPassword)
            }

            guestBtn.setOnClickListener {
                startActivity(Intent(context, SelectGameModeActivity::class.java))
            }
        }
    }

    override fun initObserver() {
        val owner = this
        loginViewModel.apply {

            loginResult.observe(owner) {
                onLoginResult(it)
            }

            usernameHasError.observe(owner) {
                displayUsernameErrorState(it)
            }

            passwordHasError.observe(owner) {
                displayPasswordErrorState(it)
            }

            userFoundByUsername.observe(owner) {
                if (!it.isNullUser()) {
//                            val intent = Intent(owner, StartActivity::class.java)
//                            intent.putExtra("User", it)
//                            startActivity(intent)
                    LoggedInUser.user = it
                    Timber.d("-------------------  ${LoggedInUser.user}")
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                }
            }
        }

    }


    private fun onLoginResult(status: String) {
        when (status) {
            LOGIN_SUCCEED -> {
                Timber.d("login success")
                Toast.makeText(applicationContext, "Login successful", Toast.LENGTH_SHORT).show()
                loginViewModel.findUser(binding.username.editText!!.text.toString().trim())
            }

            LOGIN_INCORRECT_PASSWORD -> {
                binding.apply {
                    password.error = "Incorrect Password"
                    password.requestFocus()
                }
            }

            LOGIN_USERNOTFOUND -> {
                binding.apply {
                    username?.error = "Incorrect username"
                    username.requestFocus()
                }
            }
        }
    }


    private fun displayUsernameErrorState(error: Boolean) {
        when (error) {
            true -> binding.username?.error = "Field cannot be Empty"
            false -> {
                binding.username?.error = null
                binding.username?.isErrorEnabled = false
            }
        }
    }

    private fun displayPasswordErrorState(error: Boolean) {
        when (error) {
            true -> binding.password?.error = "Field cannot be Empty"
            false -> {
                binding.password?.error = null
                binding.password?.isErrorEnabled = false
            }
        }
    }


    override fun bind(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }
}
