package com.cpen391.flappyUI

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.databinding.ActivityEndGamePointBinding
import com.cpen391.flappybluetooth.util.BluetoothConnectionUtil
/**
 *  EndGamePointActivity
 *
 *  @note: display final game score on UI, user is able to re-start game,
 *  or go check personal data list
 *
 *  @author Robin Lai
 */
class EndGamePointActivity : MvvmActivity<ActivityEndGamePointBinding>() {
    private val context: Context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()
        binding.currentGameScore.text = intent.getIntExtra("game_score", 0).toString()
    }

    override fun initView() {
        super.initView()
        binding.apply {
            startAgain.setOnClickListener {
                BluetoothConnectionUtil.getInstance().sendMessage(context, "1")
                startActivity(Intent(context, SingleGameStartActivity::class.java))
                finish()
            }
            profileIcon.setOnClickListener {
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