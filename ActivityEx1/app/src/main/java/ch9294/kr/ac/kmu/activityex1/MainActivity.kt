package ch9294.kr.ac.kmu.activityex1

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val SECOND_ACTIVITY = 100 // 두번째 액티비티 요청코드
    val REGISTER_ACTIVITY = 200 // 회원가입 액티비티 요청코드

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginBtn.setOnClickListener { v ->
            val _id = editId.text.toString()
            val _pw = editPw.text.toString()
            val intent = Intent(this, SecondActivity::class.java)

            intent.putExtra("_id", _id)
            intent.putExtra("_pw", _pw)

            startActivityForResult(intent, SECOND_ACTIVITY)
        }

        signBtn.setOnClickListener { v ->
            val intent = Intent(this, RegisterActivity::class.java)
            startActivityForResult(intent, REGISTER_ACTIVITY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            SECOND_ACTIVITY -> {
                when (resultCode) {
                    Activity.RESULT_OK -> Toast.makeText(this, "올바르게 종료되었습니다.", Toast.LENGTH_SHORT).show()
                    Activity.RESULT_CANCELED -> Toast.makeText(this, "잘못된 방법으로 종료되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            REGISTER_ACTIVITY -> {
                when (resultCode) {
                    Activity.RESULT_OK -> Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    Activity.RESULT_CANCELED -> Toast.makeText(this, "완료하지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
