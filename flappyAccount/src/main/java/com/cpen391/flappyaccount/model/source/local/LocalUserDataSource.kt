package com.cpen391.flappyaccount.model.source.local

import com.cpen391.flappyaccount.model.source.remote.RemoteUserDataSource

class LocalUserDataSource {

    companion object {
        val INSTANCE: LocalUserDataSource by lazy { LocalUserDataSource() }
    }
}