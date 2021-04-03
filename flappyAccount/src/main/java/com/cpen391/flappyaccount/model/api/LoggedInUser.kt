package com.cpen391.flappyaccount.model.api

import com.cpen391.flappyaccount.model.bean.User


object LoggedInUser {
    lateinit var user: User

    fun getLoggedInUser() : User {
        return user
    }

    fun setLoggedInUser(value: User) {
        user = value
    }
}