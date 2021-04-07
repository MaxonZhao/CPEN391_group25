package com.cpen391.flappyUI

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.R
import com.cpen391.flappyaccount.databinding.ActivityTappingBinding
import com.cpen391.flappybluetooth.activity.BluetoothConnectionService
import com.cpen391.flappybluetooth.util.BluetoothConnectionUtil
import timber.log.Timber

class TappingActivity : MvvmActivity<ActivityTappingBinding>() {
    val context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()

        binding.btnsPress.setOnClickListener {
            BluetoothConnectionUtil.getInstance().sendMessage(context, "1")
            Timber.d("++++++++++++")
            Timber.d("1")
            Timber.d("++++++++++++")


        }
        when (intent.getStringExtra("bird_color")) {
            "re" -> {
                binding.btnsPress.setImageResource(R.drawable.bird_red)
            }
            "bk" -> {
                binding.btnsPress.setImageResource(R.drawable.bird_black)
            }
            "or" -> {
                binding.btnsPress.setImageResource(R.drawable.bird_orange)
            }
            "gr" -> {
                binding.btnsPress.setImageResource(R.drawable.bird_green)
            }
            "ye" -> {
                binding.btnsPress.setImageResource(R.drawable.bird_yellow)
            }
            "bu" -> {
                binding.btnsPress.setImageResource(R.drawable.bird_blue)
            }
            else -> {
                binding.btnsPress.setImageResource(R.drawable.bird_red)
            }
        }
    }

    override fun initObserver() {

        BluetoothConnectionService.ended.observe(this) {
            val endGame = Intent(context, EndGamePointActivity::class.java)
            endGame.putExtra("game_score", BluetoothConnectionService.ended.value)
            startActivity(endGame)
        }
    }

    override fun bind(): ActivityTappingBinding {
        return ActivityTappingBinding.inflate(layoutInflater)
    }
}