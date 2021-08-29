package com.example.a26_8_2021

import android.app.Application
import com.zing.zalo.zalosdk.oauth.ZaloSDKApplication

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ZaloSDKApplication.wrap(this);

    }

}