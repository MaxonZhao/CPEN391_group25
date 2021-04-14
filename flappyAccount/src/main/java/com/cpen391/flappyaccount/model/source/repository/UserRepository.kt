package com.cpen391.flappyaccount.model.source.repository

import com.cpen391.flappyaccount.consts.LoginStatus
import com.cpen391.flappyaccount.model.bean.User
import com.cpen391.flappyaccount.model.source.UserDataSource
import com.cpen391.flappyaccount.model.source.local.LocalUserDataSource
import com.cpen391.flappyaccount.model.source.remote.RemoteUserDataSource
import io.reactivex.Observable

/**
 *  RemoteUserDataSource
 *  @note: standard implementation of MVVM architecture to access remote database
 *  and backend api service
 *
 *  @singleton design: only one database is needed in the context of this project
 *  @autho Yuefeng Zhao
 */

class UserRepository private constructor(
    private val remoteUserDataSource: RemoteUserDataSource,
    private val localUserDataSource: LocalUserDataSource
) : UserDataSource {

    override fun login(username: String, password: String): Observable<LoginStatus> {
        // tested against network connection
        return remoteUserDataSource.login(username, password)
    }

    override fun registerUser(user: User): Observable<Boolean> {
        return remoteUserDataSource.registerUser(user)
    }

    override fun findUser(username: String): Observable<User> {
        return remoteUserDataSource.findUser(username)
    }

    override fun resetPassword(user: User, newPassword: String): Observable<Boolean> {
        return remoteUserDataSource.resetPassword(user, newPassword)
    }

    override fun updateTopThreeScore(user: User, scores: List<Long>): Observable<Boolean> {
        return remoteUserDataSource.updateTopThreeScore(user, scores);
    }

    companion object {
        private lateinit var INSTANCE: UserRepository

        fun getInstance(
            remoteUserDataSource: RemoteUserDataSource,
            localUserDataSource: LocalUserDataSource
        ): UserRepository {
            if (!::INSTANCE.isInitialized) {
                INSTANCE = UserRepository(remoteUserDataSource, localUserDataSource)
            }
            return INSTANCE
        }
    }
}