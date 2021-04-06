package com.cpen391.flappyUI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.R
import com.cpen391.flappyaccount.databinding.ActivitySingleGameStartBinding
import com.cpen391.flappyaccount.viewmodel.GameSettingsViewModel
import com.cpen391.flappybluetooth.activity.MainActivity.actionStart

class SingleGameStartActivity: MvvmActivity<ActivitySingleGameStartBinding>() {

    private val context: Context = this
    private val gameSettingaViewModel by viewModels<GameSettingsViewModel>()
    private lateinit var spinner : Spinner
    private lateinit var myIntent:Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()
        myIntent = Intent(context, PersonalDataActivity::class.java)
        spinner = findViewById(R.id.select_bird_color)
        ArrayAdapter.createFromResource(
            this,
            R.array.bird_color,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = CustomOnItemSelectedListener(myIntent, binding.birdImage)
    }

    override fun initView() {
        super.initView()
        binding.apply {
            startYes.setOnClickListener {
                gameSettingaViewModel.onColorRadioButtonClicked(findViewById(R.id.select_bird_color))
                gameSettingaViewModel.onDiffLevelRadioButtonClicked(findViewById(R.id.diff_level_radio_btn))
                gameSettingaViewModel.onRadioButtonClicked(findViewById(R.id.radio_btn))
            }
            profileIcon.setOnClickListener{
                startActivity(myIntent)
            }
        }
    }

    override fun initObserver() {
        val owner = this
        gameSettingaViewModel.apply {
            birdColor.observe(owner, {
                GameSettings.instance?.setBirdColor(it)
            })
            diffLevel.observe(owner, {
                GameSettings.instance?.setDiffLevel(it)
            })
            isTapped.observe(owner, {
                tappingOrVoice(it)
            })
        }
    }

    private fun tappingOrVoice(isTapped: Boolean){
        var loginMode: String
        if(LoggedInUser.instance?.isLogin() == true){
            loginMode = "p"
        }
        else{
            loginMode = "g"
        }
        System.out.println(isTapped)
        GameSettings.instance?.setControlMethod(isTapped)
        when(isTapped){
            true -> actionStart(
                context,
                GameSettings.instance?.getBirdColor()?.substring(0,1),
                GameSettings.instance?.getBirdColor()?.substring(1,2),
                GameSettings.instance?.getDiffLevel(),
                loginMode,
                true
            )
            false -> actionStart(
                context,
                GameSettings.instance?.getBirdColor()?.substring(0,1),
                GameSettings.instance?.getBirdColor()?.substring(1,2),
                GameSettings.instance?.getDiffLevel(),
                loginMode,
                false
            )
        }
    }

    override fun bind(): ActivitySingleGameStartBinding {
        return ActivitySingleGameStartBinding.inflate(layoutInflater)
    }



    class CustomOnItemSelectedListener(private val imageIntent: Intent, private val img: ImageView) : Activity(), AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
            when(parent.getItemAtPosition(pos).toString()){

                "Red" -> {
                    img.setImageResource(R.drawable.bird_red)
                    imageIntent.putExtra("birdImage", "bird_red")
                }
                "Black" -> {
                    img.setImageResource(R.drawable.bird_black)
                    imageIntent.putExtra("birdImage", "bird_black")
                }
                "Orange" -> {
                    img.setImageResource(R.drawable.bird_orange)
                    imageIntent.putExtra("birdImage", "bird_orange")
                }
                "Green" -> {
                    img.setImageResource(R.drawable.bird_green)
                    imageIntent.putExtra("birdImage", "bird_green")
                }
                "Yellow" -> {
                    img.setImageResource(R.drawable.bird_yellow)
                    imageIntent.putExtra("birdImage", "bird_yellow")
                }
                "Blue" -> {
                    img.setImageResource(R.drawable.bird_blue)
                    imageIntent.putExtra("birdImage", "bird_blue")
                }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>) {
        }
    }


}


