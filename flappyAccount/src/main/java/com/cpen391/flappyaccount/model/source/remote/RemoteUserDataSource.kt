package com.cpen391.flappyaccount.model.source.remote

import com.cpen391.flappyaccount.consts.LoginStatus
import com.cpen391.flappyaccount.model.api.UserAPI
import com.cpen391.flappyaccount.model.bean.User
import com.cpen391.flappyaccount.model.source.UserDataSource
import com.cpen391.flappyaccount.model.source.repository.UserRepository
import io.reactivex.Observable

class RemoteUserDataSource : UserDataSource {

    companion object {
        val INSTANCE: RemoteUserDataSource by lazy { RemoteUserDataSource() }
    }

    override fun login(username: String, password: String): Observable<LoginStatus> {
        return UserAPI.login(username, password)
    }

    override fun registerUser(user: User): Observable<Boolean> {
        return UserAPI.registerUser(user)
    }

    override fun findUser(username: String): Observable<User> {
        return UserAPI.findUser(username)
    }

    override fun resetPassword(user: User, newPassword: String): Observable<Boolean> {
        return UserAPI.resetPassword(user, newPassword)
    }

    override fun updateTopThreeScore(user: User, scores: List<Long>): Observable<Boolean> {
        return UserAPI.updateTopThreeScore(user, scores)
    }
}