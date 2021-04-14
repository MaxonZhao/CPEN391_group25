package com.cpen391.businessbase.application

import com.cpen391.appbase.application.FlappyBirdApp
import com.cpen391.appbase.network.ServiceCreator
import com.cpen391.businessbase.consts.HttpConst

class BusinessApp : FlappyBirdApp() {

    override fun onCreate() {
        super.onCreate()
        initNetwork()
    }

    private fun initNetwork() {
        ServiceCreator.initBaseURL(HttpConst.BASE_URL)
    }
}