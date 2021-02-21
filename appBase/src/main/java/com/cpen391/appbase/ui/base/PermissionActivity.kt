package com.cpen391.appbase.ui.base

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.cpen391.appbase.permissions.PermissionsInterface
import com.cpen391.appbase.ui.binding.BaseBindingActivity
import timber.log.Timber
import java.util.ArrayList

abstract class PermissionActivity<T : ViewBinding> : BaseBindingActivity<T>(),
    PermissionsInterface {

    private val needPermissionsDelegate by lazy(LazyThreadSafetyMode.NONE) {
        needPermissions
    }

    //判断是否需要检测，防止无限弹框申请权限
    private var isNeedCheckPermission = true
    private val settingDialogTipPart: String
        get() {
            val deniedPermissions = getDeniedPermissions(needPermissionsDelegate)
            val stringBuilder = StringBuilder()
            for (permission in deniedPermissions!!) {
                val permissionSuffix = "android.permission."
                stringBuilder.append(permission.substring(permissionSuffix.length) + ",")
            }
            return stringBuilder.substring(0, stringBuilder.length - 1)
        }

    override fun onResume() {
        super.onResume()
        if (isNeedCheckPermission) {
            checkAllNeedPermissions()
        }
    }

    /**
     * 检查是否所有必须权限已经授予并且申请所有未授权的权限
     */
    protected fun checkAllNeedPermissions() {
        Timber.i("checkAllNeedPermissions")
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        val needRequestPermissionList = getDeniedPermissions(needPermissionsDelegate)
        if (!needRequestPermissionList.isNullOrEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                needRequestPermissionList.toTypedArray(),
                REQUEST_CODE_PERMISSION
            )
        }
    }

    /**
     * 获取`permissions`中需要申请的列表
     *
     * @param permissions 需要检测的权限列表
     * @return 需要申请的权限列表
     */
    private fun getDeniedPermissions(permissions: Array<String>?): List<String>? {
        if (permissions.isNullOrEmpty()) {
            return null
        }
        val needRequestPermissionList: MutableList<String> =
            ArrayList()
        for (permission in permissions) {
            Timber.i(
                "%s%s%s%s%s",
                permission,
                ": ",
                ContextCompat.checkSelfPermission(this, permission),
                " , ",
                ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
            )
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    permission
                )
            ) {
                needRequestPermissionList.add(permission)
            }
        }
        Timber.i("needRequestPermissionList: %s", needRequestPermissionList)
        return needRequestPermissionList
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        paramArrayOfInt: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            Timber.i("onRequestPermissionsResult")
            if (!verifyPermissionGrantResults(paramArrayOfInt)) {
                onPermissionGrantedFailed()
                showSettingDialog()
                isNeedCheckPermission = false
            } else {
                onPermissionGrantedSuccess()
            }
        }
    }

    /**
     * 检测权限申请授予结果，返回是否全部授权
     *
     * @param grantResults
     * @return
     */
    private fun verifyPermissionGrantResults(grantResults: IntArray): Boolean {
        for (grantResult in grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    /**
     * 显示提示对话框
     */
    protected fun showSettingDialog() {
        AlertDialog.Builder(this).setTitle("提示信息")
            .setMessage("当前应用缺少" + settingDialogTipPart + "权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
            .setNegativeButton("取消") { dialog: DialogInterface?, which: Int ->
                finish()
            }.setPositiveButton("确定") { dialog: DialogInterface?, which: Int ->
                startAppSettings()
                isNeedCheckPermission = true
            }.show()
            .setCancelable(false)
    }

    private fun startAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:$packageName")
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    override fun onPermissionGrantedFailed() {}

    override fun onPermissionGrantedSuccess() {}

    companion object {
        private const val REQUEST_CODE_PERMISSION = 9520
    }
}