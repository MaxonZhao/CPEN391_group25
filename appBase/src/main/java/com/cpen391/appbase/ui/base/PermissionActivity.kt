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


    //check if testing is needed to prevent it from keeping popping up tip dialogs
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
     * check if all required permissions are granted and apply for all permissions have not granted
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
     * request all permissions  in `permissions` list
     *
     * @param permissions list of permissions to check
     * @return list of permissions to apply
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
     * check status of all permissions and return true if so
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
     * display tip dialog
     */
    protected fun showSettingDialog() {
        AlertDialog.Builder(this).setTitle("reminder")
            .setMessage("Current application needs" + settingDialogTipPart + ", this functionality cannot be applied. Please click OK to go setting")
            .setNegativeButton("Cancel") { dialog: DialogInterface?, which: Int ->
                finish()
            }.setPositiveButton("OK") { dialog: DialogInterface?, which: Int ->
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