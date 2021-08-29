package com.example.a26_8_2021.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.a26_8_2021.ui.profile.google.GoogleProfileActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class GoogleLoginHelper {
    fun initGoogleSignClient(context: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
        val account = GoogleSignIn.getLastSignedInAccount(context)

        return mGoogleSignInClient
    }

    fun handleSignInResult(
        completedTask: Task<GoogleSignInAccount>,
        context: Context
    ) {  // google login
        try {
            val account = completedTask.getResult<ApiException>(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            startActivity(context, Intent(context, GoogleProfileActivity::class.java), null)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(context, "Failed due to ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}