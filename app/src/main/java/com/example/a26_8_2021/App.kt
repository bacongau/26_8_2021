package com.example.a26_8_2021

import android.app.Application
import com.zing.zalo.zalosdk.oauth.ZaloSDKApplication

class App : Application() {

    private lateinit var instance: App

    override fun onCreate() {
        super.onCreate()
        ZaloSDKApplication.wrap(this);

        instance = this
    }

    fun getInstance(): App {
        return instance
    }
}