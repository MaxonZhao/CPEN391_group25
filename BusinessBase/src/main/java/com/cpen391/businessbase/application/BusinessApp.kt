package com.cpen391.businessbase.application

import com.cpen391.appbase.application.FlappyBirdApp

class BusinessApp : FlappyBirdApp() {

    override fun onCreate() {
        super.onCreate()
        initNetwork()
    }

    private fun initNetwork() {
        //TODO: initialize network
    }
}