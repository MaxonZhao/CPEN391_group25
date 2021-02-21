package com.cpen391.appbase.ui.binding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.cpen391.appbase.ui.base.BaseFragment

abstract class BaseBindingFragment<T : ViewBinding> : BaseFragment() {
    protected lateinit var binding: T

    protected abstract fun bind(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) : T
}