package com.example.a26_8_2021.utils

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.a26_8_2021.custom.LoadingDialog
import com.example.a26_8_2021.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import java.security.AccessController.getContext

class FirebaseSignUp {

    fun firebaseSignUp(firebaseAuth: FirebaseAuth, email: String, password: String,loadingDialog: LoadingDialog,context: Context) {
        // show progress dialog
        loadingDialog.show()

        // create account
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // sign up success
                loadingDialog.dismiss()
                // get current user
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(context, "Account created with email ${email}", Toast.LENGTH_LONG)
                    .show()

                // open profile
                startActivity(context, Intent(context,LoginActivity::class.java),null)
            }
            .addOnFailureListener {
                // sign up failed
                loadingDialog.dismiss()
                Toast.makeText(context, "Sign Up failed due to ${it.message}", Toast.LENGTH_LONG)
                    .show()
            }
    }
}