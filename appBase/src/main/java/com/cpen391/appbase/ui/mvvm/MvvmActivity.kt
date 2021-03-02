package com.cpen391.appbase.ui.mvvm

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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