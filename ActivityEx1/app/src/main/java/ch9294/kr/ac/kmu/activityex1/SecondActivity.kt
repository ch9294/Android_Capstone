package ch9294.kr.ac.kmu.activityex1

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : AppCompatActivity() {

    val DATA_ACTIVITY = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val _id = intent.getStringExtra("_id")
        val _pw = intent.getStringExtra("_pw")

        textView.text = "아이디 : ${_id}"
        textView2.text = "비밀번호 : ${_pw}"

        finishBtn.setOnClickListener { v ->
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
