package com.example.a26_8_2021.utils

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.a26_8_2021.custom.LoadingDialog
import com.example.a26_8_2021.ui.login.LoginActivity
import com.example.a26_8_2021.ui.profile.firebase.FirebaseProfileActivity
import com.google.firebase.auth.FirebaseAuth
import java.security.AccessController.getContext

class FirebaseLoginHelper {

    fun firebaseLogin(
        loadingDialog: LoadingDialog,
        firebaseAuth: FirebaseAuth,
        email: String,
        password: String,
        context: Context
    ) {  // firebase login
        // show progress
        loadingDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // login success
                loadingDialog.dismiss()
                // get user info
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(context, "Logged in as $email", Toast.LENGTH_LONG).show()

                // go to profile screen
                startActivity(context, Intent(context, FirebaseProfileActivity::class.java), null)
            }
            .addOnFailureListener {
                // login failed
                loadingDialog.dismiss()
                Toast.makeText(context, "Login faild due to ${it.message}", Toast.LENGTH_LONG)
                    .show()
            }
    }
}