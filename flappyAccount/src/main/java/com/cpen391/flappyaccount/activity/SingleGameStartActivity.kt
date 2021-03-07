package com.cpen391.flappyaccount.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.R
import com.cpen391.flappyaccount.databinding.ActivitySingleGameStartBinding
import com.cpen391.flappyaccount.viewmodel.LoginViewModel
import com.cpen391.flappyaccount.viewmodel.VoiceControlViewModel

class SingleGameStartActivity: MvvmActivity<ActivitySingleGameStartBinding>() {

    private val context: Context = this
    private val singleGameViewModel by viewModels<VoiceControlViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()
    }

    override fun initView() {
        super.initView()
        binding.apply {
            startYes.setOnClickListener {
                singleGameViewModel.onRadioButtonClicked(findViewById(R.id.radio_btn))
            }
            startNo.setOnClickListener {
                startActivity(Intent(context, SelectGameModeActivity::class.java))
            }
            profileIcon.setOnClickListener{
                startActivity(Intent(context, PersonalDataActivity::class.java))
            }
        }
    }

    override fun initObserver() {
        val owner = this
        singleGameViewModel.apply {
            isTapped.observe(owner, {
                tappingOrVoice(it)
            })
        }
    }

    private fun tappingOrVoice(istapped: Boolean){
        when(istapped){
            true-> startActivity(Intent(context, StartActivity::class.java))
            false-> startActivity(Intent(context, VoiceControlActivity::class.java))
        }
    }

    override fun bind(): ActivitySingleGameStartBinding {
        return ActivitySingleGameStartBinding.inflate(layoutInflater)
    }
}

