package com.example.a26_8_2021.ui.profile.google

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.a26_8_2021.R
import com.example.a26_8_2021.ui.login.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.activity_google_profile.*

class GoogleProfileActivity : AppCompatActivity() {
    private val TAG = "GoogleProfileActivity"

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_profile)

        // configure progress dialog

        // get GoogleSignInClient
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // get info
        val acct: GoogleSignInAccount = GoogleSignIn.getLastSignedInAccount(this)
        Log.d(TAG, "onCreate: ${acct.displayName} ---  ${acct.email}")

        // update ui
        tv_google_account_info.text = "DisplayName: ${acct.displayName} \n " +
                "Email: ${acct.email}"

        // handle click sign out
        btn_google_signout.setOnClickListener {
            mGoogleSignInClient.signOut()
                .addOnSuccessListener {
                    Toast.makeText(this, "Sign out successful", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this,LoginActivity::class.java))
                }
        }

        // handle click disconnect
        btn_google_disconnect.setOnClickListener {
            mGoogleSignInClient.revokeAccess()
                .addOnSuccessListener {
                    Toast.makeText(this, "Disconnect successful", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this,LoginActivity::class.java))
                }
        }
    }
}