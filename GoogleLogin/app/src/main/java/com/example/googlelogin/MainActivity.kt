package com.example.googlelogin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        const val RC_SIGN_IN = 1000
    }

    lateinit var mAuth: FirebaseAuth
    lateinit var mGoogleApiClient: GoogleApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("668454986903-bji6adqc6reo9u8fgt7cqqeckbl1ee5u.apps.googleusercontent.com")
            .requestEmail()
            .build()

        /*
        구글 계정 인증을 하기 위한 변수
         */
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()

        /*
        Firebase 인증을 사용할 수 있게 초기화 해준다.
         */
        mAuth = FirebaseAuth.getInstance()

        googleLoginBtn.setOnClickListener {
            val signIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
            startActivityForResult(signIntent, RC_SIGN_IN)
        }

        googleLogoutBtn.setOnClickListener {
            Toast.makeText(this, "로그아웃", Toast.LENGTH_SHORT).show()
            mAuth.signOut()
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
                        Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(this, "인증 실패", Toast.LENGTH_SHORT).show()
                    }
                    false -> {
                        Toast.makeText(this, "구글 로그인 인증 성공", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this,NextActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
    }

}
