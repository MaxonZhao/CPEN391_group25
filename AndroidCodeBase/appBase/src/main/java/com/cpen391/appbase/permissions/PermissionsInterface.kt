package com.cpen391.appbase.permissions

interface PermissionsInterface {
    val needPermissions: Array<String>?
    fun onPermissionGrantedSuccess()
    fun onPermissionGrantedFailed()
}