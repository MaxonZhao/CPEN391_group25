package com.cpen391.appbase.network

import android.text.TextUtils
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/*
 **
    Singleton class to create Retrofit object for API service class
 */
class ServiceCreator private constructor() {

    private val mRetrofit: Retrofit

    fun <T> createAPI(serviceClass: Class<T>): T = mRetrofit.create(serviceClass)
    // sample usage: val appService = ServiceCreator.create(AppService::class.java) + following network calls

    companion object {
        private lateinit var BASE_URL: String

        val instance: ServiceCreator by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ServiceCreator()
        }

        @JvmStatic
        fun initBaseURL(url: String) {
            BASE_URL = url
        }
    }

    init {
        check(!TextUtils.isEmpty(BASE_URL)) { "initBaseUrl() must be called for initialization" }
        mRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // support RxJava
            .addConverterFactory(GsonConverterFactory.create()) // support Gson
            .build()
    }
}