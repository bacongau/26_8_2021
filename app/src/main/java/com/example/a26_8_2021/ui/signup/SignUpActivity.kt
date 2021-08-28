package com.example.a26_8_2021.ui.signup

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.a26_8_2021.R
import com.example.a26_8_2021.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    // Action bar
    private lateinit var actionBar: ActionBar

    // Progress Dialog
    private lateinit var progressDialog: ProgressDialog

    // Firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth

    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // configure action bar // enable back button
        actionBar = supportActionBar!!
        actionBar.title = "Sign Up"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)

        // configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Creating account in ...")
        progressDialog.setCanceledOnTouchOutside(false)

        // init firebase auth
        firebaseAuth  = FirebaseAuth.getInstance()

        // handle click begin signup
        btn_sign_up.setOnClickListener {
            // validate data
            validateData()
        }
    }

    private fun validateData() {
        // get data
        email = edt_signup_email.text.toString().trim()
        password = edt_signup_password.text.toString().trim()

        // validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            // invalid email format
            edt_signup_email.error = "Email is not valid"
        }else if (TextUtils.isEmpty(password)){
            edt_signup_password.error = "Please enter password"
        }else if (password.length < 6){
            edt_signup_password.error = "Password must at least 6 characters long"
        }else{
            // data is valide, continue sign up
            firebaseSignUp()
        }
    }

    private fun firebaseSignUp() {
        // show progress dialog
        progressDialog.show()

        // create account
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                // sign up success
                progressDialog.dismiss()
                // get current user
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this,"Account created with email ${email}", Toast.LENGTH_LONG).show()

                // open profile
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                // sign up failed
                progressDialog.dismiss()
                Toast.makeText(this,"Sign Up failed due to ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()  // go back to previous activity, when ack button of actionbar clicked
        return super.onSupportNavigateUp()
    }
}