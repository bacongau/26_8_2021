package com.example.a26_8_2021.ui.profile.zalo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.ActionBar
import com.bumptech.glide.Glide
import com.example.a26_8_2021.R
import com.example.a26_8_2021.ui.login.LoginActivity
import com.zing.zalo.zalosdk.oauth.ZaloSDK
import kotlinx.android.synthetic.main.activity_zalo_profile.*
import org.json.JSONObject

class ZaloProfileActivity : AppCompatActivity() {

    private val TAG = "ZaloProfileActivity"

    // action bar
    private lateinit var actionBar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zalo_profile)

        // configure action bar
        actionBar = supportActionBar!!
        actionBar.title = "Zalo Profile"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)

        // get profile
        ZaloSDK.Instance.getProfile(this, { data: JSONObject ->
            tv_zalo_uid?.text = "ID Người dùng: " + data.optString("id")
            tv_zalo_name?.text = "Tên người dùng: " + data.optString("name")

            val pic = data.optJSONObject("picture")
            val picData = pic?.optJSONObject("data")
            val url = picData?.optString("url")
            if (!TextUtils.isEmpty(url)) {
                Glide.with(this)
                    .load(url)
                    .into(img_zalo_avatar)
            }
        }, arrayOf("id", "name", "picture"))

        // handle click logout
        btn_zalo_logout.setOnClickListener {
            ZaloSDK.Instance.unauthenticate()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()  // go back to previous activity, when ack button of actionbar clicked
        return super.onSupportNavigateUp()
    }
}