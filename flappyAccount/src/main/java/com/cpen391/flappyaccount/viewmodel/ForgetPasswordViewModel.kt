package com.cpen391.flappyaccount.viewmodel

import androidx.lifecycle.MutableLiveData
import com.cpen391.appbase.network.SimpleObserver
import com.cpen391.appbase.ui.mvvm.BaseViewModel
import com.cpen391.flappyaccount.Injection
import com.cpen391.flappyaccount.model.bean.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class ForgetPasswordViewModel : BaseViewModel() {

    val userFoundByUsername: MutableLiveData<User> = MutableLiveData()

    fun findUser(username: String) {
        Injection.provideUserRepository().findUser(username)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SimpleObserver<User>() {
                override fun onError(e: Throwable) {
                    Timber.d("please connect to network")
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun whenSuccess(t: User) {
                    userFoundByUsername.value = t
                }
            })
    }
}