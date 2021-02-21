package com.cpen391.appbase.ui.binding

import android.os.Bundle
import android.os.PersistableBundle
import androidx.viewbinding.ViewBinding
import com.cpen391.appbase.ui.base.BaseActivity

abstract class BaseBindingActivity<T: ViewBinding> : BaseActivity() {
    protected val binding by lazy(LazyThreadSafetyMode.NONE) { bind() }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(binding.root)
    }

    protected abstract fun bind(): T
}