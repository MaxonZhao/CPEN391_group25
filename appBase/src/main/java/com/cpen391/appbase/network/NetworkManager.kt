package com.cpen391.appbase.network

class NetworkManager {

    companion object {
        private lateinit var BASE_URL: String

        @JvmStatic
        fun initBaseURL(url: String) {
            BASE_URL = url
        }
    }
}