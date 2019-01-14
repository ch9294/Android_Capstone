package ch9294.kr.ac.kmu.inserttest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener { v ->
            val search = SearchThread()
            search.start()
        }

//        button2.setOnClickListener { v ->
//
//        }
    }


    inner class SearchThread : Thread() {
        override fun run() {
            Log.d("test", "SearchThread Success!")

            val client = OkHttpClient()
            val builder = Request.Builder()
            val url = builder.url("http://13.125.170.17/ConnectTest.php")
            val request = url.build()

            client.newCall(request).enqueue(SearchCallback())
        }
    }

    inner class SearchCallback : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.d("test", "Connect Failed!")
            runOnUiThread {
                textView.text = "Disconnected!"
            }
        }

        override fun onResponse(call: Call, response: Response) {
            Log.d("test", "Connect Success!")
            val result = JSONObject(response.body()?.string())
            val obj_arr = result.getJSONArray("webnautes")

            runOnUiThread {
                for (idx in 0..(obj_arr.length() - 1)) {
                    val _idx = obj_arr.getJSONObject(idx).getString("id");
                    val _name = obj_arr.getJSONObject(idx).getString("name");
                    val _address = obj_arr.getJSONObject(idx).getString("address");

                    textView.text = "idx : ${_idx}\n"
                    textView.append("name : ${_name}\n")
                    textView.append("address : ${_address}\n\n")
                }
            }

            Log.d("test", "SearchThread exit!")
        }
    }

    inner class InsertThread : Thread() {
        override fun run() {

        }
    }

    inner class InsertCallback : Callback {
        override fun onFailure(call: Call, e: IOException) {
        }

        override fun onResponse(call: Call, response: Response) {
        }
    }
}
