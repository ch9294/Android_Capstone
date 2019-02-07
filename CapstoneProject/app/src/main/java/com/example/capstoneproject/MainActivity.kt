package com.example.capstoneproject

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {


    companion object {
        const val RC_SIGN_IN = 1000
    }

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mGoogleApiClient: GoogleApiClient

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val authClientCode = "668454986903-c38qqa2ed2imca85duuagq1qkeliei6a.apps.googleusercontent.com"

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(authClientCode)
            .requestEmail()
            .build()

        mGoogleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()

        mAuth = FirebaseAuth.getInstance()

        googleSignInBtn.setOnClickListener {
            val signIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
            startActivityForResult(signIntent, RC_SIGN_IN)
        }

    }

    override fun onStart() {
        super.onStart()
        val user = mAuth.currentUser
        user?.let {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RC_SIGN_IN -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                        when(result.isSuccess){
                            true->{
                                val account = result.signInAccount
                                firebaseAuthWithGoogle(account!!)
                            }
                        }
                    }
                    Activity.RESULT_CANCELED -> {
                    }
                }
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct:GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(acct.idToken,null)
        mAuth.signInWithCredential(credential).addOnCompleteListener {
            when(!it.isSuccessful){
                true->{
                    toast("로그인 실패").show()
                }
                false->{
                    toast("로그인 성공").show()
                    startActivity(intentFor<HomeActivity>())
                }
            }
        }
    }
}
