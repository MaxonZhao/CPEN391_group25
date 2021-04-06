package com.cpen391.flappyaccount.viewmodel

import android.app.Activity
import android.view.View
import android.widget.AdapterView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.lifecycle.MutableLiveData
import com.cpen391.appbase.ui.mvvm.BaseViewModel
import com.cpen391.flappyaccount.R

class GameSettingsViewModel: BaseViewModel() {
    var isTapped: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var diffLevel: MutableLiveData<String> = MutableLiveData<String>()
    var birdColor: MutableLiveData<String> = MutableLiveData<String>()

    fun onRadioButtonClicked(view: View) {
        if (view is RadioGroup) {
            // Is the button now checked?
            val button_name = view.checkedRadioButtonId

            // Check which radio button was clicked
            when (button_name) {
                R.id.voice_control_btn -> isTapped.value = false
                R.id.tapping_btn -> isTapped.value = true
            }
        }
    }

    fun onColorRadioButtonClicked(view: View) {
        if (view is Spinner) {
            val color_button_name = view.selectedItem
            when (color_button_name) {
                "Red" -> birdColor.value = "re"
                "Black" -> birdColor.value = "bk"
                "Orange" -> birdColor.value = "or"
                "Green" -> birdColor.value = "gr"
                "Yellow" -> birdColor.value = "ye"
                "Blue" -> birdColor.value = "bu"
            }
        }
    }


    fun onDiffLevelRadioButtonClicked(view: View) {
        if (view is RadioGroup) {
            // Is the button now checked?
            val diff_level_button_name = view.checkedRadioButtonId

            // Check which radio button was clicked
            when (diff_level_button_name) {
                R.id.easy_mode -> diffLevel.value = "e"
                R.id.medium_mode -> diffLevel.value = "m"
                R.id.hard_mode -> diffLevel.value = "h"
            }
        }
    }
}