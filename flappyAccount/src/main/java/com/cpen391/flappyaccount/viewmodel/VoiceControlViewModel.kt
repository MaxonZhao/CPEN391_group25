package com.cpen391.flappyaccount.viewmodel

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.MutableLiveData
import com.cpen391.appbase.ui.mvvm.BaseViewModel
import com.cpen391.flappyaccount.R

class VoiceControlViewModel: BaseViewModel() {
    var isTapped: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

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



}