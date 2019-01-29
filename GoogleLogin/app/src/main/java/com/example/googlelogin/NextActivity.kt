package com.example.googlelogin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_next.*

class NextActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next)
        button.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser

            if(user != null){
                textView.text = user.displayName
            }
            else{
                textView.text = "현재 사용자가 없습니다."
            }
        }
        button2.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
        }
    }
}
