package com.cpen391.flappyaccount.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.consts.PASSWORD_EMPTY
import com.cpen391.flappyaccount.consts.PASSWORD_NOT_MATCH
import com.cpen391.flappyaccount.consts.PASSWORD_TOO_WEAK
import com.cpen391.flappyaccount.consts.PASSWORD_VALID
import com.cpen391.flappyaccount.databinding.ActivityResetPasswordBinding
import com.cpen391.flappyaccount.model.bean.User
import com.cpen391.flappyaccount.viewmodel.ResetPasswordViewModel
import com.cpen391.flappyaccount.viewmodel.VerifyOTPViewModel

class ResetPasswordActivity : MvvmActivity<ActivityResetPasswordBinding>() {

    private val resetPasswordViewModel by viewModels<ResetPasswordViewModel>()
    private lateinit var userFound: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()
        userFound = intent.getSerializableExtra("User") as User
    }

    override fun initView() {
        super.initView()
        binding.resetBtn.setOnClickListener {
            var newPassword: String = binding.newPassword.editText?.text.toString()
            var confirmPassword: String = binding.confirmPassword.editText?.text.toString()
            if (newPassword == null) newPassword = ""
            if (confirmPassword == null) confirmPassword = ""
            resetPasswordViewModel.resetPassword(userFound, newPassword, confirmPassword)
        }
    }

    override fun initObserver() {
        val owner = this
        resetPasswordViewModel.apply {
            newPasswordHasError.observe(owner, {
                displayNewPasswordErrorState(it)
            })

            confirmPasswordHasError.observe(owner, {
                displayConfirmPasswordErrorState(it)
            })

            resetPasswordResult.observe(owner, {
                onResetPasswordResult(it)
            })
        }
    }

    override fun bind(): ActivityResetPasswordBinding {
        return ActivityResetPasswordBinding.inflate(layoutInflater)
    }



    private fun displayNewPasswordErrorState(error: String) {
        when (error) {
            PASSWORD_EMPTY -> binding.newPassword.error = "Field cannot be Empty"
            PASSWORD_TOO_WEAK -> binding.newPassword.error = "password is too weak"
            PASSWORD_VALID -> {
                binding.newPassword.error = null
                binding.newPassword.isErrorEnabled = false
            }
        }
    }


    private fun displayConfirmPasswordErrorState(error: String) {
        when (error) {
            PASSWORD_EMPTY -> binding.confirmPassword?.error = "Field cannot be Empty"
            PASSWORD_NOT_MATCH -> binding.confirmPassword?.error = "Password do not match"
            PASSWORD_VALID -> {
                binding.confirmPassword?.error = null
                binding.confirmPassword?.isErrorEnabled = false
            }
        }
    }


    private fun onResetPasswordResult(success: Boolean) {
        if (success) {
            startActivity(Intent(this, ResetPasswordSuccessActivity::class.java))
        } else {
            Toast.makeText(this, "Cannot reset your password", Toast.LENGTH_SHORT)
        }
    }


    companion object {
        fun actionStart(context: Context, data1: User) {
            val intent = Intent(context, ResetPasswordActivity::class.java)
            intent.putExtra("User", data1)
            context.startActivity(intent)
        }
    }
}