package com.cpen391.appbase.ui.mvvm

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.cpen391.appbase.ui.base.PermissionActivity

abstract class MvvmActivity<T : ViewBinding> : PermissionActivity<T>() {

    override val needPermissions: Array<String>?
        get() = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }

    protected abstract fun initObserver()

}