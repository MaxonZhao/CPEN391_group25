package com.cpen391.flappyaccount.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.R
import com.cpen391.flappyaccount.databinding.ActivitySingleGameStartBinding

class SingleGameStartActivity: MvvmActivity<ActivitySingleGameStartBinding>() {

    private val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()
    }

    override fun initView() {
        super.initView()
        binding.apply {
            startYes.setOnClickListener {
                startActivity(Intent(context, StartActivity::class.java))
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
    }

    override fun bind(): ActivitySingleGameStartBinding {
        return ActivitySingleGameStartBinding.inflate(layoutInflater)
    }
}

private fun setOnClickListener(startActivity: Unit) {

}
