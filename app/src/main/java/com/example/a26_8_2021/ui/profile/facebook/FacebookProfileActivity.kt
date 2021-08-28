package com.example.a26_8_2021.ui.profile.facebook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.a26_8_2021.R
import com.example.a26_8_2021.ui.login.LoginActivity
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import kotlinx.android.synthetic.main.activity_facebook_profile.*


class FacebookProfileActivity : AppCompatActivity() {
    private val TAG = "FacebookProfileActivity"

    // action bar
    private lateinit var actionBar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facebook_profile)

        // configure action bar
        actionBar = supportActionBar!!
        actionBar.title = "Facebook Profile"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)

        // get data
        val bundle = intent.extras
        val name = bundle?.getString("name")
        val email = bundle?.getString("email")
        val pic = bundle?.getString("profilePic")

        Log.d(TAG, pic+"")
        Glide.with(this)
            .load(pic)
            .apply( RequestOptions().override(200, 200))
            .into(img_fb_avatar)
        tv_fb_name.text = "Name: $name"
        tv_fb_email.text = "Email: $email"

        btn_facebook_logout.setOnClickListener {
            LoginManager.getInstance().logOut()
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()  // go back to previous activity, when ack button of actionbar clicked
        return super.onSupportNavigateUp()
    }
}