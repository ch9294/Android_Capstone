package ch9294.kr.ac.kmu.json

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView.text = ""
        button.setOnClickListener { v ->
            val thread = JsonThread()
            thread.start()
        }
    }

    inner class JsonThread : Thread() {
        override fun run() {
            Log.d("jsonTest", "Thread Start!")
            val client = OkHttpClient()
            val url = Request.Builder().url("http://13.125.170.17/jsonTest.php")
            val request = url.build()

            client.newCall(request).enqueue(JsonCallback())
        }
    }

    inner class JsonCallback : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.d("jsonTest", "Connect Failed!")
        }

        override fun onResponse(call: Call, response: Response) {

            Log.d("jsonTest", "Connect Success!")
            val result = response.body()?.string()
            val jsonArray = JSONObject(result).getJSONArray("json_data")

            for (idx in 0..(jsonArray.length() - 1)) {
                val obj = jsonArray.getJSONObject(idx)

                val data1 = obj.getString("data1")
                val data2 = obj.getInt("data2")
                val data3 = obj.getDouble("data3")

                runOnUiThread {
                    textView.append("data1 : ${data1}\n")
                    textView.append("data2 : ${data2}\n")
                    textView.append("data3 : ${data3}\n\n")
                }
            }
        }
    }
}
