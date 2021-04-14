package com.cpen391.flappyaccount.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.ActionBar
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyUI.EndGamePointActivity
import com.cpen391.flappyaccount.databinding.ActivityStartBinding

class StartActivity : MvvmActivity<ActivityStartBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()
        Handler().postDelayed(Runnable {
            startActivity(
                Intent(
                    this,
                    EndGamePointActivity::class.java
                )
            )
        }, 3000);

    }

    override fun initObserver() {

    }

    override fun bind(): ActivityStartBinding {
        return ActivityStartBinding.inflate(layoutInflater)
    }
}
