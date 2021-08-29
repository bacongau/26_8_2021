package com.example.a26_8_2021.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import com.example.a26_8_2021.ui.profile.facebook.FacebookProfileActivity
import com.facebook.FacebookCallback
import com.facebook.login.LoginResult
import org.json.JSONObject

class FacebookLoginHelper {
    fun getFacebookData(context: Context,obj: JSONObject?) {
        val profilePic =
            "https://graph.facebook.com/${obj?.getString("id")}/picture?width=200&height=200"
        val name = obj?.getString("name")
        val email = obj?.getString("email")

        val bundle = Bundle()
        bundle.putString("name", name)
        bundle.putString("email", email)
        bundle.putString("profilePic", profilePic)

        var intent = Intent(context, FacebookProfileActivity::class.java)
        intent.putExtras(bundle)
        startActivity(context, intent, null)
    }
}