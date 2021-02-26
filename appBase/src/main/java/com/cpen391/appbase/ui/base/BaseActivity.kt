package com.cpen391.appbase.ui.base

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber
import androidx.annotation.LayoutRes as LayoutRes

abstract class BaseActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("${this.javaClass.name} onCreate")
    }

    override fun onStart() {
        super.onStart()
        Timber.d("${this.javaClass.name} onStart")
    }

    override fun onResume() {
        super.onResume()
        Timber.d("${this.javaClass.name} onResume")
    }

    override fun onPause() {
        super.onPause()
        Timber.d("${this.javaClass.name} onPause")
    }

    override fun onStop() {
        super.onStop()
        Timber.d("${this.javaClass.name} onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("${this.javaClass.name} onDestroy")
    }

    override fun setContentView(@LayoutRes layoutResID: Int) {
        super.setContentView(layoutResID)
        initView()
    }

    override fun setContentView(view: View) {
        super.setContentView(view)
        initView()
    }

    override fun setContentView(
        view: View,
        params: ViewGroup.LayoutParams
    ) {
        super.setContentView(view, params)
        initView()
    }

    protected open fun initView() {}
}