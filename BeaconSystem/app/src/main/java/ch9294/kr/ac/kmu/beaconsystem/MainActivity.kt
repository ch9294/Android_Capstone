package ch9294.kr.ac.kmu.beaconsystem

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.jetbrains.anko.*
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private val SIGNUP_ACTIVITY = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        loginInfoLoad()

        inputID.setOnKeyListener(EnterKeyEventListener())
        inputPW.setOnKeyListener(EnterKeyEventListener())

        loginBtn.setOnClickListener {
            val selectList = listOf(inputID, inputPW)

            selectList.forEach {
                if (it.text.toString().equals("")) {
                    toast("아이디 및 비밀번호를 모두 입력해주세요").show()
                    it.requestFocus()
                    return@setOnClickListener
                }
            }

            val loginThread = LoginThread()
            loginThread.start()
        }

        signUpBtn.setOnClickListener {
            startActivityForResult(intentFor<SignUpActivity>(), SIGNUP_ACTIVITY)
        }

        exitBtn.setOnClickListener {
            System.exit(1)
        }
    }


    /*
    메인 액티비티에 존재하는 각각의 입력창에서 엔터키를 눌렀을 경우
    처리할 이벤트
     */
    inner class EnterKeyEventListener : View.OnKeyListener {
        override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
            when (v!!.id) {
                inputID.id -> {
                    if(keyCode == event?.keyCode) {
                        // 비밀번호 입력창으로 포커스 할당
                        inputPW.requestFocus()
                    }
                    return true
                }
                inputPW.id -> {
                    if(keyCode == event?.keyCode) {
                        // 가상 키보드를 내린다
                        val hideKeyBoard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        hideKeyBoard.hideSoftInputFromWindow(v.windowToken, 0)
                    }
                    return true
                }
            }
            return true
        }
    }

    /*
    메인 액티비티의 onCreate 메소드가 호출되면서 사용자의 아이디 및 비밀번호 정보를 가지고 있는지 확인한다.
    만약 가지고 있다면 각각의 입력창에 아이디와 비밀번호 정보가 입력이 된 상태로 메인 액티비티가 나타남
     */
    private fun loginInfoLoad() {
        val auth = getSharedPreferences("loginAuth", Context.MODE_PRIVATE)
        if (auth.getBoolean("check", false)) {
            inputID.setText(auth.getString("id", ""))
            inputPW.setText(auth.getString("pw", ""))
            loginBtn.performClick()
            return
        }
        toast("로그 : 쿠키 정보가 없습니다")
    }


    /*
    메인 액티비티로 다시 돌아올 경우
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            SIGNUP_ACTIVITY -> {
                when (requestCode) {
                    Activity.RESULT_OK -> {
                        toast(R.string.toast7).show()
                        inputID.setText(intent.getStringExtra("id"))
                        inputPW.setText(intent.getStringExtra("pw"))
                    }
                    Activity.RESULT_CANCELED -> {
                        toast(R.string.toast12).show()
                    }
                }
            }
        }
    }

    /*
    로그인 버튼을 클릭하면 발생하는 로그인 쓰레드
     */
    inner class LoginThread : Thread() {
        override fun run() {
            val client = OkHttpClient()
            val url = Request.Builder().url("http://13.125.170.17/loginApp.php")
            val bodyBuilder = FormBody.Builder()

            val id = inputID.text.toString()
            val pw = inputPW.text.toString()

            bodyBuilder.add("userId", id)
            bodyBuilder.add("userPw", pw)
            val body = bodyBuilder.build()

            val request = url.post(body).build()

            client.newCall(request).enqueue(LoginCallback())
        }
    }

    /*
    로그인 쓰레드에서 서버로 요청하고 응답을 받을 콜백
     */
    inner class LoginCallback : Callback {

        override fun onFailure(call: Call, e: IOException) {
            runOnUiThread {
                toast("연결 실패").show()
            }
        }

        override fun onResponse(call: Call, response: Response) {
            val result = response.body()?.string()
            when (result) {
                "SUCCESS_LOGIN" -> {
                    /*
                    로그인 쿠키 정보를 저장한다.
                     */
                    val auth = getSharedPreferences("loginAuth", Context.MODE_PRIVATE)
                    val edit = auth.edit()
                    edit.apply {
                        putString("id", inputID.text.toString())
                        putString("pw", inputPW.text.toString())
                        putBoolean("check", true)
                    }.apply()
                    startActivity(intentFor<CentralActivity>().clearTop().clearTask())
                }

                "NOT_EQUAL_PASSWORD" -> {
                    runOnUiThread {
                        alert("비밀번호가 맞지않습니다", "아이디 및 패스워드 오류") {
                            yesButton {
                                inputPW.text = null
                            }
                        }.show()
                    }
                }

                "NOT_SEARCH_ID" -> {
                    runOnUiThread {
                        alert("아이디가 맞지 않습니다", "아이디 및 패스워드 오류") {
                            yesButton {
                                inputID.text = null
                                inputPW.text = null
                            }
                        }.show()
                    }
                }
            }
        }

    }
}


