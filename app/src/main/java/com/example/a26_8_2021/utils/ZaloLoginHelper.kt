package com.example.a26_8_2021.utils

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.a26_8_2021.ui.profile.zalo.ZaloProfileActivity
import com.zing.zalo.zalosdk.oauth.LoginVia
import com.zing.zalo.zalosdk.oauth.OAuthCompleteListener
import com.zing.zalo.zalosdk.oauth.OauthResponse
import com.zing.zalo.zalosdk.oauth.ZaloSDK
import java.security.AccessController.getContext

class ZaloLoginHelper {


    fun onLoginSuccess(context: Context) {  // zalo login
        val intent = Intent(context, ZaloProfileActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(context, intent, null)
    }

    fun onLoginError(code: Int, message: String, context: Context) {   // zalo login
        Toast.makeText(context, "[$code] $message", Toast.LENGTH_LONG).show()
    }


}