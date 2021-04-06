package com.cpen391.flappyUI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.R
import com.cpen391.flappyaccount.databinding.ActivitySingleGameStartBinding
import com.cpen391.flappyaccount.viewmodel.GameSettingsViewModel

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
        birdColor = colorBit(spinner.selectedItem.toString())
        spinner.onItemSelectedListener = CustomOnItemSelectedListener()
    }

    override fun initView() {
        super.initView()
        binding.apply {
            startYes.setOnClickListener {
                gameSettingaViewModel.onRadioButtonClicked(findViewById(R.id.radio_btn))
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
            diffLevel.observe(owner, {
                diffLevelData(it)
            })
        }
    }

    private fun tappingOrVoice(isTapped: Boolean){
        when(isTapped){
            true -> startActivity(Intent(context, EndGamePointActivity::class.java))
            false -> startActivity(Intent(context, EndGamePointActivity::class.java))
        }
    }

    fun colorBit(color: String): String {
        val color_button_name = color
        var colorBit: String = "ye"
        when (color_button_name) {
            "Red" -> colorBit = "re"
            "Black" -> colorBit = "bk"
            "Orange" -> colorBit = "or"
            "Green" -> colorBit = "gr"
            "Yellow" -> colorBit = "ye"
            "Blue" -> colorBit = "bu"
        }
        return colorBit
    }

    private fun diffLevelData(diffLevel: String){
        when(diffLevel){
//            "e"-> startActivity(Intent(context, EndGamePointActivity::class.java))
//            "m"-> startActivity(Intent(context, EndGamePointActivity::class.java))
//            "h"-> startActivity(Intent(context, EndGamePointActivity::class.java))
        }
    }

    override fun bind(): ActivitySingleGameStartBinding {
        return ActivitySingleGameStartBinding.inflate(layoutInflater)
    }
}

class CustomOnItemSelectedListener : Activity(), AdapterView.OnItemSelectedListener {
    private val context: Context = this
    private var imageView: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_game_start)
        imageView = findViewById(R.id.bird_image)
    }
    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
//        imageView?.setImageResource(R.drawable.bird_red)
        when(parent.getItemAtPosition(pos).toString()){
            "Red" -> imageView?.setImageResource(R.drawable.bird_red)
            "Black" -> imageView?.setImageResource(R.drawable.bird_red)
            "Orange" -> imageView?.setImageResource(R.drawable.bird_orange)
            "Green" -> imageView?.setImageResource(R.drawable.bird_green)
            "Yellow" -> imageView?.setImageResource(R.drawable.bird_yellow)
            "Blue" -> imageView?.setImageResource(R.drawable.bird_blue)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
    }
}

