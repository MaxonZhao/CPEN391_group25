package com.cpen391.flappyUI

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.databinding.ActivityPersonalDataBinding

class PersonalDataActivity  : MvvmActivity<ActivityPersonalDataBinding>() {
    private val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()
        binding.userName.text = LoggedInUser.user.fullName
        binding.userEmail.text = LoggedInUser.user.email
        binding.top1Score.text = LoggedInUser.user.top_three_scores[0].toString()
        binding.top2Score.text = LoggedInUser.user.top_three_scores[1].toString()
        binding.top3Score.text = LoggedInUser.user.top_three_scores[2].toString()
    }

    override fun initObserver() {
    }

    override fun bind(): ActivityPersonalDataBinding {
        return ActivityPersonalDataBinding.inflate(layoutInflater)
    }

}