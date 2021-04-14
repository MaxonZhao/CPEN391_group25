package com.cpen391.flappyUI.util

import com.cpen391.flappyaccount.model.bean.User
/**
 *  LoggedInUser
 *
 *  @note: Util file, provide User data to activities
 *
 *  @author Robin Lai
 */
class LoggedInUserUtil private constructor() {
    private var userData: User? = null

    fun getUser(): User? {
        return userData
    }

    fun setUser(user: User?) {
        userData = user
    }

    fun isLogin(): Boolean {
        return (userData != null)
    }

    companion object {

        @Volatile
        private var mInstance: LoggedInUserUtil? = null

        @JvmStatic
        val instance: LoggedInUserUtil?
            get() {
                if (mInstance == null) {
                    synchronized(LoggedInUserUtil::class.java) {
                        if (mInstance == null) {
                            mInstance = LoggedInUserUtil()
                        }
                    }
                }
                return mInstance
            }
    }
}