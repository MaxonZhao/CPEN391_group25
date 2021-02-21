package com.cpen391.appbase.ui.mvvm

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.cpen391.appbase.ui.binding.BaseBindingFragment

abstract class MvvmFragment<T : ViewBinding> : BaseBindingFragment<T>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
    }

    protected abstract fun initObserver()
}