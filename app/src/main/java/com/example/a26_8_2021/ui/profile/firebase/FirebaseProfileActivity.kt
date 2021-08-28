package com.example.a26_8_2021.ui.profile.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import com.example.a26_8_2021.R
import com.example.a26_8_2021.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_firebase_profile.*

class FirebaseProfileActivity : AppCompatActivity() {
    private val TAG = "FirebaseProfileActivity"

    // action bar
    private lateinit var actionBar: ActionBar

    // firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_profile)

        // configure action bar
        actionBar = supportActionBar!!
        actionBar.title = "Firebase Profile"

        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        // handle click logout
        btn_firebase_logout.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }
    }

    private fun checkUser() {
        // check user is logged in or not
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            // user not null, user is logged in
            val email = firebaseUser.email
            val name = firebaseUser.displayName
            // set to text view
            tv_firebase_account_info.text = "DisplayName: ${name} \nEmail: ${email}"
        } else {
            // user is null, user is not logged in
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}