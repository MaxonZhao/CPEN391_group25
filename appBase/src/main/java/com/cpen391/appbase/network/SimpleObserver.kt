package com.cpen391.appbase.network

import android.accounts.NetworkErrorException
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

abstract class SimpleObserver<T> : Observer<T> {

    override fun onNext(t: T) {
        whenSuccess(t)
    }

    override fun onComplete() {}
    abstract fun whenSuccess(t: T)

}