package ch9294.kr.ac.kmu.loginandregister

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val SUBMIT = 0
        const val REGISTER = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        submitBtn.setOnClickListener { v ->

        }

        exitBtn.setOnClickListener { v ->
            System.exit(1)
        }

        registerBtn1.setOnClickListener { v ->
            val intent = Intent(this, RegisterActivity::class.java)
            startActivityForResult(intent, REGISTER)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            SUBMIT -> {

            }

            REGISTER -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                    }
                    Activity.RESULT_CANCELED -> {
                    }
                }
            }
        }
    }
}
