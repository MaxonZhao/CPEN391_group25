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

        binding.apply {

            LoggedInUser.instance?.getUser()?.run {
                binding.userName.text =  fullName
                userEmail.text = email
                top1Score.text = top_three_scores[0].toString()
                top2Score.text = top_three_scores[1].toString()
                top3Score.text = top_three_scores[2].toString()
            }

        }

    }

    override fun initObserver() {
    }

    override fun bind(): ActivityPersonalDataBinding {
        return ActivityPersonalDataBinding.inflate(layoutInflater)
    }

}