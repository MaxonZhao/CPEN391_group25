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

/**
 *  ForgetPasswordViewModel
 *
 *  @note: asynchronous call to find user from remote database,
 *  implemented using RxJava
 *
 *  @autho Yuefeng Zhao
 */
class ForgetPasswordViewModel : BaseViewModel() {

    val userFoundByUsername: MutableLiveData<User> = MutableLiveData()

    // update live data when user is found successfully. An observer is registered in its activity
    // and it will perform corresponding UI operation based on the user fetched
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