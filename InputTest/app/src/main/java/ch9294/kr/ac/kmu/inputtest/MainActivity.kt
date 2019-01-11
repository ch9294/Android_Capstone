package ch9294.kr.ac.kmu.inputtest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener { v ->
            val inputThread = InputThread()
            inputThread.start()
        }
    }

    inner class InputThread : Thread() {
        override fun run() {
            try {
                val client = OkHttpClient()
                val url = Request.Builder().url("http://13.125.170.17/InputTest.php")
                val bodyBuilder = FormBody.Builder()

                bodyBuilder.add("name", editText.text.toString())
                bodyBuilder.add("address", editText2.text.toString())
                val body = bodyBuilder.build()
                val post = url.post(body)

                val request = post.build()

                Log.d("InputTest", "HTTP 연결 실행")
                client.newCall(request).enqueue(InputCallback())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    inner class InputCallback : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.d("InputTest", "연결 실패")

            runOnUiThread {
                textView.text = """
                    연결에 실패했습니다.
                    ${e.printStackTrace()}
                """.trimIndent()
            }

        }

        override fun onResponse(call: Call, response: Response) {
            Log.d("InputTest", "연결 성공")


            runOnUiThread {
                Toast.makeText(this@MainActivity, "연결이 성공했습니다.", Toast.LENGTH_SHORT).show()
                textView.text = """
                    name : ${editText.text}
                    address : ${editText2.text}
                """.trimIndent()
            }
        }
    }
}
