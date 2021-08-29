package com.example.a26_8_2021.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.ViewModelProvider
import com.example.a26_8_2021.R
import com.example.a26_8_2021.base.BaseActivity
import com.example.a26_8_2021.databinding.ActivityLoginBinding
import com.example.a26_8_2021.ui.profile.facebook.FacebookProfileActivity
import com.example.a26_8_2021.ui.profile.firebase.FirebaseProfileActivity
import com.example.a26_8_2021.ui.profile.google.GoogleProfileActivity
import com.example.a26_8_2021.ui.profile.zalo.ZaloProfileActivity
import com.example.a26_8_2021.ui.signup.SignUpActivity
import com.example.a26_8_2021.utils.FacebookLoginHelper
import com.example.a26_8_2021.utils.FirebaseLoginHelper
import com.example.a26_8_2021.utils.GoogleLoginHelper
import com.example.a26_8_2021.utils.ZaloLoginHelper
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.zing.zalo.zalosdk.oauth.LoginVia
import com.zing.zalo.zalosdk.oauth.OAuthCompleteListener
import com.zing.zalo.zalosdk.oauth.OauthResponse
import com.zing.zalo.zalosdk.oauth.ZaloSDK
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import java.security.AccessController.getContext

const val RC_SIGN_IN = 123

class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>() {
    private val TAG = "LoginActivity"

    // action bar
    private lateinit var actionBar: ActionBar

    // Firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var callBackManager: CallbackManager

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private var email = ""
    private var password = ""


    override fun createViewModel(): LoginViewModel {
        val factory = LoginViewModelFactory()
        return ViewModelProvider(this, factory).get(LoginViewModel::class.java)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun initView() {
        // configure actionbar
        actionBar = supportActionBar!!
        actionBar.title = "Login"

        /////////// start facebook login
        callBackManager = CallbackManager.Factory.create()
        btn_fb_login.setReadPermissions(
            listOf(
                "email",
                "public_profile",
                "user_gender",
                "user_birthday",
                "user_friends"
            )
        )
        btn_fb_login.registerCallback(callBackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                val graphRequest =
                    GraphRequest.newMeRequest(result?.accessToken) { `object`, response ->
                        FacebookLoginHelper().getFacebookData(btn_fb_login.context, `object`)
                    }

                val parameter = Bundle()
                parameter.putString("fields", "name,email")
                graphRequest.parameters = parameter

                graphRequest.executeAsync()
            }

            override fun onCancel() {}

            override fun onError(error: FacebookException?) {}
        })
    }

    override fun initData() {
        // initGoogleSignClient
        mGoogleSignInClient = GoogleLoginHelper().initGoogleSignClient(this)

        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun initListener() {
        // click google login
        btn_google_login.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        // handle click begin firebase login
        btn_login.setOnClickListener {
            // before loggin in, validate date
            validateData()
        }

        // handle click firebase sign up
        btn_go_to_sign_up.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        // handle click zalo login
        btn_zalo_login.setOnClickListener {
            loginZalo()
        }
    }

    private val listener = object : OAuthCompleteListener() {   // zalo login
        override fun onGetOAuthComplete(response: OauthResponse?) {
            if (TextUtils.isEmpty(response?.oauthCode)) {
                ZaloLoginHelper().onLoginError(
                    response?.errorCode ?: -1,
                    response?.errorMessage ?: "Unknown Error",
                    btn_fb_login.context
                )
            } else {
                ZaloLoginHelper().onLoginSuccess(btn_fb_login.context)
            }
        }
    }

    private fun loginZalo() {
        ZaloSDK.Instance.authenticate(this, LoginVia.APP_OR_WEB, listener)
    }

    private fun validateData() { // firebase login
        // get data
        email = edt_login_email.text.toString().trim()
        password = edt_login_password.text.toString().trim()

        // validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // invalid email format
            textInputLayout_email.error = "Email not valid"
        } else if (TextUtils.isEmpty(password)) {
            textInputLayout_password.error = "Please enter password"
        } else {
            // data is validated, begin login
            FirebaseLoginHelper().firebaseLogin(loadingDialog, firebaseAuth, email, password, this)
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) { // google login
        super.onActivityResult(requestCode, resultCode, data)

        callBackManager.onActivityResult(requestCode, resultCode, data) // facebook login

        ZaloSDK.Instance.onActivityResult(this, requestCode, resultCode, data)   // zalo login

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);  /// google login
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            GoogleLoginHelper().handleSignInResult(task, this)
        }
    }
}