package com.cpen391.appbase.network

import io.reactivex.Observer

abstract class SimpleObserver<T> : Observer<T> {

    override fun onNext(t: T) {
        whenSuccess(t)
    }

    override fun onComplete() {}
    abstract fun whenSuccess(t: T)

}