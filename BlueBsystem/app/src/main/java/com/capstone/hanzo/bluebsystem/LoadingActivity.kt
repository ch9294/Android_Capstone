package com.capstone.hanzo.bluebsystem

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import org.jetbrains.anko.intentFor

class LoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_loading)
        loading()
    }

    private fun loading(){
        Handler().postDelayed(
            Runnable {
                startActivity(intentFor<LoginActivity>())
                finish()
            }
        ,700)
    }
}
