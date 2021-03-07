package com.cpen391.flappyaccount.model.source.repository

import com.cpen391.flappyaccount.consts.LoginStatus
import com.cpen391.flappyaccount.model.bean.User
import com.cpen391.flappyaccount.model.source.UserDataSource
import com.cpen391.flappyaccount.model.source.local.LocalUserDataSource
import com.cpen391.flappyaccount.model.source.remote.RemoteUserDataSource
import io.reactivex.Observable

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

    companion object {
        private lateinit var INSTANCE: UserRepository

        fun getInstance (
            remoteUserDataSource: RemoteUserDataSource,
            localUserDataSource: LocalUserDataSource
        ) : UserRepository {
            if (!::INSTANCE.isInitialized) {
                INSTANCE = UserRepository(remoteUserDataSource, localUserDataSource)
            }
            return INSTANCE
        }
    }
}