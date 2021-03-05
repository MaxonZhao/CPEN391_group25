package com.cpen391.flappyaccount.model.source

import com.cpen391.flappyaccount.consts.LoginStatus
import com.cpen391.flappyaccount.model.bean.User
import io.reactivex.Observable

interface UserDataSource {
    fun login(username: String, password: String) : Observable<LoginStatus>

    fun registerUser(user: User) : Observable<Boolean>

    fun findUser(username: String) : Observable<User>
}