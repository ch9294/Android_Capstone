package ch9294.kr.ac.kmu.connectserver

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.SimpleAdapter
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG_JSON = "webnautes"
        private const val TAG_ID = "id"
        private const val TAG_NAME = "name"
        private const val TAG_ADDRESS = "address"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var thread = ConnectThread()
        thread.start()
    }

    inner class ConnectThread : Thread() {
        override fun run() {
            try {
                Log.d("ConnectTest", "Thread Start!")

                // 클라이언트 생성
                val client = OkHttpClient()

                // url 설정
                val url = Request.Builder().url("http://13.125.170.17/ConnectTest.php")

                // 요청 객체 생성
                val requst = url.build()

                // 서버에 http 요청 처리하기
                client.newCall(requst).enqueue(ConnectCallback())
                Log.d("ConnectTest", "Complete!")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    inner class ConnectCallback : Callback {
        override fun onFailure(call: Call, e: IOException) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onResponse(call: Call, response: Response) {
            Log.d("ConnectTest", "Http Connect Success!")
            val result = JSONObject(response.body()?.string())
            val arrData = result.getJSONArray(TAG_JSON)
            val map = ArrayList<Map<String, String>>()

            for (idx in 0..(arrData.length() - 1)) {
                val id = arrData.getJSONObject(idx).getString("id")
                val name = arrData.getJSONObject(idx).getString("name")
                val address = arrData.getJSONObject(idx).getString("address")

                map.add(mapOf(TAG_ID to id, TAG_NAME to name, TAG_ADDRESS to address))
            }

            runOnUiThread {
                Log.d("ConnectTest", "runOnUiThread Success")
                val adapter = SimpleAdapter(
                    this@MainActivity, map, R.layout.item_list,
                    arrayOf(TAG_ID, TAG_NAME, TAG_ADDRESS),
                    intArrayOf(R.id.textView_list_id, R.id.textView_list_name, R.id.textView_list_address)
                )

                listView_main_list.adapter = adapter
            }
        }
    }
}
