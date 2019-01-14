package ch9294.kr.ac.kmu.loginandregister

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.*
import java.io.IOException

class RegisterActivity : AppCompatActivity() {

    val inputList = listOf<View>(editName, editId2, editPassword1, radioGender, editAge)
    val confirmId: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        editAge.setOnSeekBarChangeListener(MySeekbarListener())

        registerBtn2.setOnClickListener { v ->

            for (v in inputList) {
                when (v) {
                    editName -> {
                        if (editName.text.equals("")) {
                            Toast.makeText(this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }
                    }
                    editId2 -> {
                        if (editId2.text.equals("")) {
                            Toast.makeText(this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }
                    }
                    editPassword1 -> {
                        if (editId2.text.equals("")) {
                            Toast.makeText(this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }
                    }
                    else -> {
                    }
                }
            }

            if (!confirmId) {
                val registerThread = RegisterThread()
                registerThread.start()
            } else {
                Toast.makeText(this, "아이디 중복 확인을 해주세요!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 회원가입 과정이 완료되었다.
            setResult(Activity.RESULT_OK)

            // 로그인 화면으로 돌아간다.
            finish()
        }

        resetBtn.setOnClickListener { v ->
            editName.clearComposingText()
            editId2.clearComposingText()
            editPassword2.clearComposingText()
            radioGender.clearCheck()
            editAge.progress = 1
            Toast.makeText(this, "초기화 완료!", Toast.LENGTH_SHORT).show()
        }

        cancelBtn.setOnClickListener { v ->
            Toast.makeText(this, "취소!", Toast.LENGTH_SHORT).show()
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        confirmIdBtn.setOnClickListener { v ->

        }
    }

    inner class RegisterThread : Thread() {
        override fun run() {
            val client = OkHttpClient()
            val url = Request.Builder().url("http://13.125.170.17/registerTest.php")
            val bodyBuilder = FormBody.Builder()

            bodyBuilder.add("_name", editName.text.toString())
            bodyBuilder.add("_id", editId2.text.toString())
            bodyBuilder.add("_password", editPassword2.text.toString())
            bodyBuilder.add("_gender", radioGender.check(radioGender.checkedRadioButtonId).toString())
            bodyBuilder.add("_name", editAge.progress.toString())

            val body = bodyBuilder.build()
            val request = url.post(body).build()

            client.newCall(request).enqueue(RegisterCallback())
        }
    }

    inner class RegisterCallback : Callback {
        override fun onFailure(call: Call, e: IOException) {
            runOnUiThread {
                Toast.makeText(this@RegisterActivity, "서버 연결 실패", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onResponse(call: Call, response: Response) {

        }
    }

    inner class MySeekbarListener : SeekBar.OnSeekBarChangeListener {
        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            seekBar?.incrementProgressBy(1)
            textAge.text = "나이 : ${progress}"
        }
    }
}
