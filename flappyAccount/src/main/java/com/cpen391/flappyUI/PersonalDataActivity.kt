package com.cpen391.flappyUI

import android.content.Context
import android.os.Bundle
import androidx.annotation.MainThread
import androidx.appcompat.app.ActionBar
import com.cpen391.appbase.network.SimpleObserver
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyaccount.Injection
import com.cpen391.flappyaccount.consts.LoginStatus
import com.cpen391.flappyaccount.databinding.ActivityPersonalDataBinding
import com.cpen391.flappyaccount.model.bean.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class PersonalDataActivity  : MvvmActivity<ActivityPersonalDataBinding>() {
    private val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()

        lateinit var user: User

        LoggedInUser.instance?.getUser()?.let {
            Injection.provideUserRepository()
                .findUser(it.userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SimpleObserver<User>() {

                    override fun onError(e: Throwable) {
                        Timber.d("please connect to network")
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun whenSuccess(t: User) {
                        user = it
                    }
                })
        }

        binding.apply {

            LoggedInUser.instance?.getUser()?.run {
                binding.userName.text =  fullName
                userEmail.text = email
                top1Score.text = top_three_scores[0].toString()
                top2Score.text = top_three_scores[1].toString()
                top3Score.text = top_three_scores[2].toString()
            }

        }

    }

    override fun initObserver() {
    }

    override fun bind(): ActivityPersonalDataBinding {
        return ActivityPersonalDataBinding.inflate(layoutInflater)
    }

}