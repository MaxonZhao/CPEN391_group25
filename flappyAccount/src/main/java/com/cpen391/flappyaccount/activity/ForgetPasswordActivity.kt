package com.cpen391.flappyaccount.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.observe
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.databinding.ActivityForgetPasswordBinding
import com.cpen391.flappyaccount.viewmodel.ForgetPasswordViewModel

class ForgetPasswordActivity : MvvmActivity<ActivityForgetPasswordBinding>() {
    private val forgetPasswordViewModel by viewModels<ForgetPasswordViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()
    }

    override fun initView() {
        super.initView()
        binding.next.setOnClickListener {
            binding.apply {
                val name = username.editText!!.text.toString()
                if (name != "") {
                    username.error = null
                    username.isErrorEnabled = false
                    forgetPasswordViewModel.findUser(name)
                } else {
                    username.error = "Field cannot be empty"
                }
            }
        }
    }

    override fun initObserver() {
        val owner = this
        forgetPasswordViewModel.userFoundByUsername.observe(owner) {
            if (it!!.isNullUser()) {
                Toast.makeText(owner, "cannot find user", Toast.LENGTH_SHORT).show()
            } else {
                //                ResetPasswordActivity.actionStart(owner, it)
                VerifyOTPActivity.actionStart(owner, it!!)
            }
        }
    }

    override fun bind(): ActivityForgetPasswordBinding {
        return ActivityForgetPasswordBinding.inflate(layoutInflater)
    }

}