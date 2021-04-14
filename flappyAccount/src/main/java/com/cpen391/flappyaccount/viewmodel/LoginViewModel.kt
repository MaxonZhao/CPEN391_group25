package com.cpen391.flappyaccount.viewmodel

import androidx.lifecycle.MutableLiveData
import com.cpen391.appbase.network.SimpleObserver
import com.cpen391.appbase.ui.mvvm.BaseViewModel
import com.cpen391.flappyaccount.Injection
import com.cpen391.flappyaccount.consts.LoginStatus
import com.cpen391.flappyaccount.model.bean.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class LoginViewModel : BaseViewModel() {
    val loginResult: MutableLiveData<String> = MutableLiveData<String>()
    val usernameHasError: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val passwordHasError: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val userFoundByUsername: MutableLiveData<User> = MutableLiveData<User>()


    fun login(username: String, password: String) {
        if (!validateUsername(username) || !validatePassword(password)) {
            return
        } else {
            doLogin(username, password)
        }
    }

    private fun doLogin(username: String, password: String) {

        Injection.provideUserRepository().login(username, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SimpleObserver<LoginStatus>() {
                override fun whenSuccess(t: LoginStatus) {
                    loginResult.value = t.status
                }

                override fun onError(e: Throwable) {
                    Timber.d("please connect to network")
                }

                override fun onSubscribe(d: Disposable) {
                }
            })
    }

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


    private fun validateUsername(username: String): Boolean {
        usernameHasError.value = false || username.isEmpty()
        return !(username.isEmpty())
    }

    private fun validatePassword(password: String): Boolean {
        passwordHasError.value = false || password.isEmpty()
        return !(password.isEmpty())
    }

}