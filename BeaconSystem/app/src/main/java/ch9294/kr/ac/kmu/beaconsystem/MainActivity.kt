package ch9294.kr.ac.kmu.beaconsystem

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.jetbrains.anko.*
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private val SIGNUP_ACTIVITY = 0

    //    private var nextActivity: Intent? = null
//    private val toast = Toast(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginBtn.setOnClickListener {
            val loginThread = LoginThread()
            loginThread.start()
        }

        signUpBtn.setOnClickListener {
            //            nextActivity = Intent(this, SignUpActivity::class.java)
//            startActivityForResult(nextActivity, SIGNUP_ACTIVITY)
            startActivityForResult(intentFor<SignUpActivity>(), SIGNUP_ACTIVITY)
        }

        exitBtn.setOnClickListener {
            System.exit(1)
        }
    }

    inner class LoginThread : Thread() {
        override fun run() {
            val client = OkHttpClient()
            val url = Request.Builder().url("")
            val bodyBuilder = FormBody.Builder()

            bodyBuilder.add("userId", inputID.toString())
            bodyBuilder.add("userPw", inputPW.toString())

            val body = bodyBuilder.build()
            val request = url.post(body).build()

            client.newCall(request).enqueue(LoginCallback())
        }
    }

    inner class LoginCallback : Callback {
        override fun onFailure(call: Call, e: IOException) {
            runOnUiThread {
                UI {
                    toast("연결 실패").show()
                }
            }
        }

        override fun onResponse(call: Call, response: Response) {
            val result = response.body()?.string()

            val obj = JSONObject(result)
            val name = obj.keys()

            name.forEach {
                when (it) {
                    "successLoging" -> {
                        /*
                        로그인 쿠키 정보를 저장한다.
                         */
                        val pref = getSharedPreferences("session", Context.MODE_PRIVATE)
                        pref.edit().putString("id", inputID.toString()).apply()
                        startActivity(intentFor<CentralActivity>().clearTop().clearTask().newTask())
                    }

                    "notEqualPw" -> {
                        UI {
                            alert("비밀번호가 맞지않습니다", "아이디 및 패스워드 오류") {
                                yesButton {
                                    inputPW.text = null
                                }
                            }.show()
                        }
                    }

                    "noSearchId" -> {
                        UI {
                            alert("아이디가 맞지 않습니다", "아이디 및 패스워드 오류") {
                                yesButton {
                                    inputID.text = null
                                    inputPW.text = null
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            SIGNUP_ACTIVITY -> {
                when (requestCode) {
                    Activity.RESULT_OK -> {
                            toast("성공적으로 가입되었습니다.").show()
                            inputID.setText(intent.getStringExtra("id"))
                            inputPW.setText(intent.getStringExtra("pw"))
                    }
                    Activity.RESULT_CANCELED -> {
                            toast("가입이 취소되었습니다").show()
                    }
                }
            }
        }
    }
}