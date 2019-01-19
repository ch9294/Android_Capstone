package ch9294.kr.ac.kmu.beaconsystem

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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

    private var nextActivity: Intent? = null
    private val toast = Toast(this)

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
            startActivityForResult(intentFor<SignUpActivity>(),SIGNUP_ACTIVITY)
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
                toast.setText("연결 실패")
            }
        }

        override fun onResponse(call: Call, response: Response) {
            val result = response.body()?.string()

            val first = JSONObject(result)
            val name = first.keys()

            name.forEach {
                when (it) {
                    "successLoging" -> {
                        /*
                        로그인 쿠키 정보를 저장한다.
                         */
                        val pref = getSharedPreferences("session", Context.MODE_PRIVATE)
                        pref.edit().putString("id", inputID.toString()).apply()

                        val nextPage = Intent(this@MainActivity,CentralActivity::class.java)
                        nextPage.flags = Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK + Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }

                    "notEqualPw" -> {

                    }

                    "noSearchId" -> {

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
                        UI {
                            toast("성공적으로 가입되었습니다.")

                            intentFor<SignUpActivity>()
                        }
//                        toast.setText("성공적으로 가입되었습니다.")
//                        toast.duration = Toast.LENGTH_SHORT
//                        toast.show()

                        /*
                    가입에 성공했을 경우 회원가입에서 입력했던 아이디와 비밀번호 정보가 로그인 화면으로 전달되어
                    로그인 입력창에 입력되게 한다.(사용자의 소소한 편의성 제공)
                     */
                        inputID.setText(intent.getStringExtra("id"))
                        inputPW.setText(intent.getStringExtra("pw"))
                    }
                    Activity.RESULT_CANCELED -> {

                        UI{
                            toast("가입이 취소되었습니다")
                        }
//                        toast.setText("가입이 취소되었습니다")
//                        toast.duration = Toast.LENGTH_SHORT
//                        toast.show()
                    }
                }
            }
        }
    }
}