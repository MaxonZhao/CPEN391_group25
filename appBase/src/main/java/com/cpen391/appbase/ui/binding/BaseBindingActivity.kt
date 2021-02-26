package com.cpen391.appbase.ui.binding

import android.os.Bundle
import android.os.PersistableBundle
import androidx.viewbinding.ViewBinding
import com.cpen391.appbase.ui.base.BaseActivity
import timber.log.Timber

abstract class BaseBindingActivity<T: ViewBinding> : BaseActivity() {
    protected val binding by lazy(LazyThreadSafetyMode.NONE) { bind() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    protected abstract fun bind(): T
}