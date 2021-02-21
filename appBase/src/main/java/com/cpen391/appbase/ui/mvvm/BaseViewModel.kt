package com.cpen391.appbase.ui.mvvm

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel: ViewModel() {
    private val compositeDisposableDelegate = lazy {
        CompositeDisposable()
    }
    private val compositeDisposable by compositeDisposableDelegate

    protected fun Disposable.mark() {
        compositeDisposable.add(this)
    }

    override fun onCleared() {
        super.onCleared()
        if (compositeDisposableDelegate.isInitialized()) {
            compositeDisposable.clear()
        }
    }
}