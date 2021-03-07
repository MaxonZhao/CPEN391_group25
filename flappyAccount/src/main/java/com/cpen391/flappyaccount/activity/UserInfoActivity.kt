package com.cpen391.flappyaccount.activity

import com.cpen391.flappyaccount.model.bean.User

class UserInfoActivity private constructor() {
    //    private var mBitmap: Bitmap? = null
    private var userData: User? = null

    fun getUser() : User?{
        return userData
    }
    fun setUser(user: User){
        userData = user
    }
    fun isLogin(): Boolean{
        return (userData == null)
    }
    companion object {

        @Volatile
        private var mInstance: UserInfoActivity? = null
        @JvmStatic
        val instance: UserInfoActivity?
            get() {
                if (mInstance == null) {
                    synchronized(UserInfoActivity::class.java) {
                        if (mInstance == null) {
                            mInstance = UserInfoActivity()
                        }
                    }
                }
                return mInstance
            }
    }
}