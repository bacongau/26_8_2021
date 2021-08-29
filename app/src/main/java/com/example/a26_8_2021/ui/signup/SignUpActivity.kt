package com.example.a26_8_2021.ui.signup

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.a26_8_2021.App
import com.example.a26_8_2021.R
import com.example.a26_8_2021.base.BaseActivity
import com.example.a26_8_2021.databinding.ActivitySignUpBinding
import com.example.a26_8_2021.ui.login.LoginActivity
import com.example.a26_8_2021.utils.FirebaseSignUp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity<SignUpViewModel, ActivitySignUpBinding>() {

    // Firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth

    private var email = ""
    private var password = ""

    override fun createViewModel(): SignUpViewModel {
        val factory = SignUpViewModelFactory()
        return ViewModelProvider(this, factory).get(SignUpViewModel::class.java)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_sign_up
    }

    override fun initView() {
        binding.viewModel = viewModel;
        binding.lifecycleOwner = this;

        // configure action bar // enable back button
        actionBar.title = "Sign Up"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)

        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        // handle click begin signup
        btn_sign_up.setOnClickListener {
            // validate data and sign up
            validateData()
        }
    }

    override fun initData() {

    }

    override fun initListener() {

    }


    private fun validateData() {
        // get data
        email = edt_signup_email.text.toString().trim()
        password = edt_signup_password.text.toString().trim()

        // validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // invalid email format
            edt_signup_email.error = "Email is not valid"
        } else if (TextUtils.isEmpty(password)) {
            edt_signup_password.error = "Please enter password"
        } else if (password.length < 6) {
            edt_signup_password.error = "Password must at least 6 characters long"
        } else {
            // data is valide, continue sign up
            FirebaseSignUp().firebaseSignUp(firebaseAuth,email,password,loadingDialog,this)
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()  // go back to previous activity, when ack button of actionbar clicked
        return super.onSupportNavigateUp()
    }
}