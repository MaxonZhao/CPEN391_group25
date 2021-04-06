package com.cpen391.flappyUI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.R
import com.cpen391.flappyaccount.databinding.ActivitySingleGameStartBinding
import com.cpen391.flappyaccount.viewmodel.GameSettingsViewModel
import com.cpen391.flappybluetooth.activity.BluetoothConnectionService
import com.cpen391.flappybluetooth.activity.MainActivity
import timber.log.Timber

class SingleGameStartActivity: MvvmActivity<ActivitySingleGameStartBinding>() {

    private val context: Context = this
    private val gameSettingaViewModel by viewModels<GameSettingsViewModel>()
    private var birdColor: String = "ye"
    private lateinit var spinner : Spinner
    private lateinit var imageView : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()
        spinner = findViewById(R.id.select_bird_color)
        ArrayAdapter.createFromResource(
            this,
            R.array.bird_color,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = CustomOnItemSelectedListener(binding.birdImage)
    }

    override fun initView() {
        super.initView()
        binding.apply {
            startYes.setOnClickListener {
                gameSettingaViewModel.onRadioButtonClicked(findViewById(R.id.radio_btn))
                gameSettingaViewModel.onColorRadioButtonClicked(findViewById(R.id.select_bird_color))
                gameSettingaViewModel.onDiffLevelRadioButtonClicked(findViewById(R.id.diff_level_radio_btn))
            }
            profileIcon.setOnClickListener{
                startActivity(Intent(context, PersonalDataActivity::class.java))
            }
        }
    }

    override fun initObserver() {
        val owner = this
        gameSettingaViewModel.apply {
            isTapped.observe(owner, {
                tappingOrVoice(it)
            })
            birdColor.observe(owner, {
                selectBirdColor(it)
            })
            diffLevel.observe(owner, {
                selectDiffLevel(it)
            })
            GameSettings.instance?.setGameSettings(
                birdColor.toString(),
                diffLevel.toString(),
                isTapped.toString()
            )
        }
    }

    private fun tappingOrVoice(isTapped: Boolean){
        System.out.println(isTapped)
        GameSettings.instance?.setControlMethod(isTapped)
        when(isTapped){
            true -> startActivity(Intent(context, EndGamePointActivity::class.java))
            false -> startActivity(Intent(context, EndGamePointActivity::class.java))
        }
    }

    private fun selectBirdColor(color: String){
        System.out.println(color)
        GameSettings.instance?.setBirdColor(color)
    }

    private fun selectDiffLevel(diffLevel: String){
        System.out.println(diffLevel)
        GameSettings.instance?.setDiffLevel(diffLevel)
    }

    override fun bind(): ActivitySingleGameStartBinding {
        return ActivitySingleGameStartBinding.inflate(layoutInflater)
    }



    class CustomOnItemSelectedListener(private val img: ImageView) : Activity(), AdapterView.OnItemSelectedListener {
        private val context: Context = this

        override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {

            when(parent.getItemAtPosition(pos).toString()){

                "Red" -> img?.setImageResource(R.drawable.bird_red)
                "Black" -> img?.setImageResource(R.drawable.bird_red)
                "Orange" -> img?.setImageResource(R.drawable.bird_orange)
                "Green" -> img?.setImageResource(R.drawable.bird_green)
                "Yellow" -> img?.setImageResource(R.drawable.bird_yellow)
                "Blue" -> img?.setImageResource(R.drawable.bird_blue)
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>) {
        }
    }


}


