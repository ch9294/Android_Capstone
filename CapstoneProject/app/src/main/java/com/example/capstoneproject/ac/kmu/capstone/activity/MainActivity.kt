package com.example.capstoneproject.ac.kmu.capstone.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.capstoneproject.R
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.jetbrains.anko.*
import java.io.IOException

class MainActivity : AppCompatActivity(), AnkoLogger, GoogleApiClient.OnConnectionFailedListener {

    companion object {
        const val RC_SIGN_IN = 1000
    }

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mGoogleApiClient: GoogleApiClient

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("668454986903-bji6adqc6reo9u8fgt7cqqeckbl1ee5u.apps.googleusercontent.com")
            .requestEmail()
            .build()

        mGoogleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()

        mAuth = FirebaseAuth.getInstance()

        googleSignInBtn.setOnClickListener {
            val signIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
            startActivityForResult(signIntent,
                RC_SIGN_IN
            )
        }

    }

    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser

        user?.let {
            startActivity(intentFor<HomeActivity>())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_SIGN_IN -> {
                val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                when (result.isSuccess) {
                    true -> {
                        val account = result.signInAccount
                        firebaseAuthWithGoogle(account!!)
                    }
                    false -> {
                        toast("로그인 실패").show()
                    }
                }
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential).addOnCompleteListener {
            when (!it.isSuccessful) {
                true -> {
                    toast("로그인 실패").show()
                }
                false -> {
                    toast("로그인 성공").show()
                    doAsync {
                        val url = Request.Builder().url("http://13.125.170.17/googleUserInsert.php")
                        val body = FormBody.Builder().apply {
                            add("user_email",mAuth.currentUser?.email!!)
                            add("user_name",mAuth.currentUser?.displayName!!)
                        }.build()
                        val request = url.post(body).build()
                        OkHttpClient().newCall(request).execute()
                    }
                    startActivity(intentFor<HomeActivity>())
                }
            }
        }
    }
}
