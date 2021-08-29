package com.example.a26_8_2021.ui.profile.google

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.ViewModelProvider
import com.example.a26_8_2021.R
import com.example.a26_8_2021.base.BaseActivity
import com.example.a26_8_2021.databinding.ActivityGoogleProfileBinding
import com.example.a26_8_2021.ui.login.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.activity_google_profile.*

class GoogleProfileActivity : BaseActivity<GoogleProfileViewModel, ActivityGoogleProfileBinding>() {
    lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()  // go back to previous activity, when ack button of actionbar clicked
        return super.onSupportNavigateUp()
    }

    override fun createViewModel(): GoogleProfileViewModel {
        return ViewModelProvider(this).get(GoogleProfileViewModel::class.java)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_google_profile
    }

    override fun initView() {
        // configure action bar
        actionBar.title = "Google Profile"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    override fun initData() {
        // get GoogleSignInClient
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // get info
        val acct: GoogleSignInAccount = GoogleSignIn.getLastSignedInAccount(this)

        // update ui
        tv_google_account_info.text = "DisplayName: ${acct.displayName} \n Email: ${acct.email}"
    }

    override fun initListener() {
        // handle click sign out
        btn_google_signout.setOnClickListener {
            mGoogleSignInClient.signOut()
                .addOnSuccessListener {
                    Toast.makeText(this, "Sign out successful", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                }
        }

        // handle click disconnect
        btn_google_disconnect.setOnClickListener {
            mGoogleSignInClient.revokeAccess()
                .addOnSuccessListener {
                    Toast.makeText(this, "Disconnect successful", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                }
        }
    }
}