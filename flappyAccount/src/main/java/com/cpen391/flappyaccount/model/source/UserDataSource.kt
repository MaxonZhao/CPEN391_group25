package com.cpen391.flappyaccount.model.source

import com.cpen391.flappyaccount.consts.LoginStatus
import com.cpen391.flappyaccount.model.bean.User
import io.reactivex.Observable

/**
 *  RemoteUserDataSource
 *  @note: standard implementation of MVVM architecture to access remote database
 *  and backend api service
 *
 *  @singleton desing: only one database is needed in the context of this project
 *  @autho Yuefeng Zhao
 */

interface UserDataSource {
    fun login(username: String, password: String): Observable<LoginStatus>

    fun registerUser(user: User): Observable<Boolean>

    fun findUser(username: String): Observable<User>

    fun resetPassword(user: User, newPassword: String): Observable<Boolean>

    fun updateTopThreeScore(user: User, scores: List<Long>): Observable<Boolean>
}