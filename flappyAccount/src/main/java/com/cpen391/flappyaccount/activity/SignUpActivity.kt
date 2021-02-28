package com.cpen391.flappyaccount.activity

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.databinding.ActivitySignupBinding
import com.cpen391.flappyaccount.model.bean.User
import com.google.firebase.database.*
import timber.log.Timber


class SignUpActivity : MvvmActivity<ActivitySignupBinding>() {

//    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()

//        FirebaseApp.initializeApp(this)
        binding.registerButton.setOnClickListener{

        }

        val database = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("User")

        Timber.d(myRef.toString() + " hello there")

        val user1 = User(
            "YuefengZhao",
            "MaxonZhao",
            "maxonzhao@gmail.com",
            "15618219971",
            "Maxon1418"
        )
        myRef.setValue(user1)

    }
    override fun initObserver() {

    }

    override fun bind(): ActivitySignupBinding {
        return ActivitySignupBinding.inflate(layoutInflater)
    }
}