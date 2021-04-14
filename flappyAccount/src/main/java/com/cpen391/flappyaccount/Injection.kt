package com.cpen391.flappyaccount

import com.cpen391.flappyaccount.model.source.local.LocalUserDataSource
import com.cpen391.flappyaccount.model.source.remote.RemoteUserDataSource
import com.cpen391.flappyaccount.model.source.repository.UserRepository

/**
 *  Injection
 *
 *  @note: provide instance of UserRepository, standard implementation of MVVM architecture
 *  @autho Yuefeng Zhao
 */
object Injection {

    fun provideUserRepository(): UserRepository {
        return UserRepository.getInstance(
            RemoteUserDataSource.INSTANCE,
            LocalUserDataSource.INSTANCE
        )
    }
}