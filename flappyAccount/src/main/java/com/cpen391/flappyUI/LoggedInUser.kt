package com.cpen391.flappyUI

import com.cpen391.flappyaccount.model.bean.User

class LoggedInUser private constructor() {
    private var userData: User? = null

    fun getUser() : User?{
        return userData
    }
    fun setUser(user: User?){
        userData = user
    }
    fun isLogin(): Boolean{
        return (userData != null)
    }
    companion object {

        @Volatile
        private var mInstance: LoggedInUser? = null
        @JvmStatic
        val instance: LoggedInUser?
            get() {
                if (mInstance == null) {
                    synchronized(LoggedInUser::class.java) {
                        if (mInstance == null) {
                            mInstance = LoggedInUser()
                        }
                    }
                }
                return mInstance
            }
    }
}