package com.cpen391.flappyUI

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.databinding.ActivityEndGamePointBinding

class EndGamePointActivity : MvvmActivity<ActivityEndGamePointBinding>(){
    private val context: Context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()
        binding.currentGameScore.text = intent.getStringExtra("currentScore")
    }

    override fun initView() {
        super.initView()
        binding.apply {
            startAgain.setOnClickListener {
                startActivity(Intent(context, SingleGameStartActivity::class.java))
            }
            profileIcon.setOnClickListener{
                startActivity(Intent(context, PersonalDataActivity::class.java))
            }
        }
    }

    override fun initObserver() {
    }

    override fun bind(): ActivityEndGamePointBinding {
        return ActivityEndGamePointBinding.inflate(layoutInflater)
    }
}