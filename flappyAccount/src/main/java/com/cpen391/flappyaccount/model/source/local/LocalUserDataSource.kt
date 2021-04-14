package com.cpen391.flappyaccount.model.source.local

/**
 *  LocalUserDataSource
 *  @deprecated: Usage of the app is primarily online, only access to remote database is needed
 *
 *  @note: standard implementation of MVVM architecture, maintain a local database to fetch user info
 *  in cases where the host device is offline
 *
 *  @autho Yuefeng Zhao
 */
class LocalUserDataSource {

    companion object {
        val INSTANCE: LocalUserDataSource by lazy { LocalUserDataSource() }
    }
}