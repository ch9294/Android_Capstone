package ch9294.kr.ac.kmu.beaconsystem

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import okhttp3.*
import org.jetbrains.anko.*
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception

class SignUpActivity : AppCompatActivity() {
    private var checkId: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val selectList = listOf(editID, editPW, editPW2, editName)
        val editLitener = EnterKeyEventListener()

        editID.setOnKeyListener(editLitener)
        editPW.setOnKeyListener(editLitener)
        editPW2.setOnKeyListener(editLitener)
        editName.setOnKeyListener(editLitener)

        submitBtn.setOnClickListener {
            selectList.forEach {
                when (it.text.toString()) {
                    "" -> {
                        it.setHint(R.string.necessary_input_alert)
                        return@setOnClickListener
                    }
                    else -> Log.d("test", "${it.text}")
                }
            }
            if (checkId) {
                val signUpThread = SignUpThread()
                signUpThread.start()
            } else {
                toast(R.string.toast5).show()
            }
        }

        resetBtn.setOnClickListener {
            selectList.forEach {
                it.text = null
            }
            radioYouth.isChecked = true
            radioMale.isChecked = true
            checkId = false
        }

        checkBtn.setOnClickListener {
            val checkThread = CheckThread()
            checkThread.start()
        }

        returnBtn.setOnClickListener {
            setResult(Activity.RESULT_CANCELED, intentFor<MainActivity>().singleTop().clearTask())
            finish()
        }
    }

    inner class EnterKeyEventListener : View.OnKeyListener {
        /*
        가상 키보드에서 엔터키를 입력했을 경우
        키보드를 보이지 않게 하는 코드

        엔터키를 눌렀을 때 만약 다음 뷰로 포커스를 넘기고 싶다면
        포커싱을 주고 싶은 뷰의 메소드 requestFocus()를 호출한다
        2019-01-21 : 메소드를 호출하면 포커싱이 두번 움직임
         */
        override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
            val edit = v!! as EditText
            val hideKeyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard.hideSoftInputFromWindow(v?.windowToken,0)
                edit.text = null
                return true
            }
            return false
        }
    }

    /*
    돌아가기 버튼과 동일한 동작을 하기 때문에 일단 막아 놓는다
     */
    override fun onBackPressed() {
        toast(R.string.toast1).show()
    }

    inner class CheckThread : Thread() {
        override fun run() {

            Log.d("test", "Thread Start")
            /*
            http 클라이언트 생성
            POST방식으로 입력한 아이디를 아이디 중복 체크를 위한 서버로 전송
             */
            val client = OkHttpClient()
            val url = Request.Builder().url("http://13.125.170.17/ConfirmID.php")
            val bodyBuilder = FormBody.Builder()

            bodyBuilder.add("user_id", editID.text.toString())
            val body = bodyBuilder.build()
            val request = url.post(body).build()

            client.newCall(request).enqueue(CheckCallback())
        }
    }

    inner class CheckCallback : Callback {
//        private val SUCCESS = "success"

        override fun onFailure(call: Call, e: IOException) {
            runOnUiThread {
                toast(R.string.toast2).show()
            }
        }

        override fun onResponse(call: Call, response: Response) {
            Log.d("test", "Http Connect Success1")
            val result = response.body()?.string()
            Log.d("test", "${result}")

            when (result) {
                "NOT_OVERLAP" -> {
                    runOnUiThread {
                        alert {
                            titleResource = R.string.alert_title1
                            messageResource = R.string.toast8
                            yesButton { checkId = true }
                        }.show()
                    }
                }
                "OVERLAP" -> {
                    runOnUiThread {
                        alert {
                            titleResource = R.string.alert_title1
                            messageResource = R.string.toast9
                            yesButton {
                                editID.text = null
                                checkId = false
                            }
                        }.show()
                    }
                }
            }
        }
    }


    inner class SignUpThread : Thread() {
        override fun run() {
            val client = OkHttpClient()
            val url = Request.Builder().url("http://13.125.170.17/InputTest.php")
            val bodyBuilder = FormBody.Builder()

            /*
            성별과 나이에서 각각의 라디오 그룹에서 선택된 라디오 버튼의 객체를 가져온다
            체크된 라디오 버튼이 어떤것인지 아이디 값(정수)만 알 수 있기 때문에 이 정수 값을 이용하기 위해서는
            find() 메소드를 이용해야함
             */
            val genderChecked = find<RadioButton>(Group_gender.checkedRadioButtonId)
            val ageChecked = find<RadioButton>(Group_age.checkedRadioButtonId)

            bodyBuilder.add("user_id", editID.text.toString())
            bodyBuilder.add("user_pw", editPW.text.toString())
            bodyBuilder.add("user_name", editName.text.toString())
            bodyBuilder.add("user_sex", genderChecked.text.toString())
            bodyBuilder.add("user_age", ageChecked.text.toString())

            val body = bodyBuilder.build()
            val request = url.post(body).build()

            client.newCall(request).enqueue(SignUpCallback())
        }
    }

    inner class SignUpCallback : Callback {
        override fun onFailure(call: Call, e: IOException) {
            runOnUiThread {
                toast(R.string.toast2).show()
            }
        }

        override fun onResponse(call: Call, response: Response) {
            Log.d("test", "Http Connect Success2")
            val result = response.body()?.string()
            Log.d("test", "${result}")

            when (result) {
                "OVERLAP"->{
                    runOnUiThread {
                        toast(R.string.toast10).show()
                    }
                }
                "SUCCESS" -> {
                    runOnUiThread {
                        toast(R.string.toast7).show()
                        setResult(
                            Activity.RESULT_OK, intentFor<MainActivity>(
                                "id" to editID.text,
                                "pw" to editPW.text
                            ).singleTop().clearTask()
                        )
                        finish()
                    }
                }
                "FAIL" -> {
                    runOnUiThread {
                        toast(R.string.toast6).show()
                    }
                }
            }
        }
    }
}



