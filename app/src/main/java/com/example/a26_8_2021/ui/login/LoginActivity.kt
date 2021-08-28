package com.example.a26_8_2021.ui.login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.a26_8_2021.R
import com.example.a26_8_2021.ui.profile.facebook.FacebookProfileActivity
import com.example.a26_8_2021.ui.profile.firebase.FirebaseProfileActivity
import com.example.a26_8_2021.ui.profile.google.GoogleProfileActivity
import com.example.a26_8_2021.ui.profile.zalo.ZaloProfileActivity
import com.example.a26_8_2021.ui.signup.SignUpActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
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

const val RC_SIGN_IN = 123

class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"

    // action bar
    private lateinit var actionBar: ActionBar

    // progress dialog
    private lateinit var progressDialog: ProgressDialog

    // Firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var callBackManager: CallbackManager

    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // configure actionbar
        actionBar = supportActionBar!!
        actionBar.title = "Login"

        // configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Logging in ...")
        progressDialog.setCanceledOnTouchOutside(false)

        /////////// start google login
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        val account = GoogleSignIn.getLastSignedInAccount(this)


        btn_google_login.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
        /////////// end google login

        /////////// start firebase login
        // init firebase auth
        firebaseAuth  = FirebaseAuth.getInstance()

        // handle click begin login
        btn_login.setOnClickListener {
            // before loggin in, validate date
            validateData()
        }

        // handle click sign up
        btn_go_to_sign_up.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        /////////// end firebase login

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
                        getFacebookData(`object`)
                    }

                val parameter = Bundle()
                parameter.putString("fields", "name,email")
                graphRequest.parameters = parameter

                graphRequest.executeAsync()
                Log.d(TAG, "onSuccess: facebook log in")
            }

            override fun onCancel() {
                TODO("Not yet implemented")
            }

            override fun onError(error: FacebookException?) {
                TODO("Not yet implemented")
            }
        })
        /////////// end facebook login



        // handle click login
        btn_zalo_login.setOnClickListener {
            loginZalo()
        }
    }

    private fun onLoginSuccess() {  // zalo login
        val intent = Intent(this, ZaloProfileActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun onLoginError(code: Int, message: String) {   // zalo login
        Toast.makeText(this, "[$code] $message", Toast.LENGTH_LONG).show()
    }

    private val listener = object : OAuthCompleteListener() {   // zalo login
        override fun onGetOAuthComplete(response: OauthResponse?) {
            if (TextUtils.isEmpty(response?.oauthCode)) {
                onLoginError(response?.errorCode ?: -1, response?.errorMessage ?: "Unknown Error")
            } else {
                onLoginSuccess()
            }
        }
    }

    private fun loginZalo() {
        ZaloSDK.Instance.authenticate(this, LoginVia.APP_OR_WEB,listener)
    }

    private fun getFacebookData(obj: JSONObject?) {
        val profilePic = "https://graph.facebook.com/${obj?.getString("id")}/picture?width=200&height=200"
        val name = obj?.getString("name")
        val email = obj?.getString("email")

        val bundle = Bundle()
        bundle.putString("name",name)
        bundle.putString("email",email)
        bundle.putString("profilePic",profilePic)

        var intent = Intent(this,FacebookProfileActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun validateData() { // firebase login
        // get data
        email = edt_login_email.text.toString().trim()
        password = edt_login_password.text.toString().trim()

        // validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            // invalid email format
            edt_login_email.error = "Email not valid"
        }else if (TextUtils.isEmpty(password)){
            edt_login_password.error = "Please enter password"
        }else{
            // data is validated, begin login
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {  // firebase login
        // show progress
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // login success
                progressDialog.dismiss()
                // get user info
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this, "Logged in as $email", Toast.LENGTH_LONG).show()

                // go to profile screen
                startActivity(Intent(this, FirebaseProfileActivity::class.java))
            }
            .addOnFailureListener {
                // login failed
                progressDialog.dismiss()
                Toast.makeText(this, "Login faild due to ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { // google login
        super.onActivityResult(requestCode, resultCode, data)

        callBackManager.onActivityResult(requestCode, resultCode, data) // facebook login

        ZaloSDK.Instance.onActivityResult(this,requestCode,resultCode,data)   // zalo login

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);  /// google login
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {  // google login
        try {
            val account = completedTask.getResult<ApiException>(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            startActivity(Intent(this, GoogleProfileActivity::class.java))
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(this, "Failed due to ${e.message}", Toast.LENGTH_LONG).show()
            Log.d(TAG, "Failed due to ${e.message}")
        }
    }

}