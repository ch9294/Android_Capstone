package ch9294.kr.ac.kmu.mysqliteapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import java.io.IOException
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject

class DataController(val db: SQLiteDatabase, val context: Context) : AppCompatActivity() {

    var jsonList: ArrayList<JSONObject> = ArrayList()

    fun dataSelect() {
        val cursor = db.query("Person", null, null, null, null, null, null);

        if (cursor.isNull(0)) {
            Toast.makeText(context, "데이터가 없습니다.", Toast.LENGTH_SHORT).show()
            DataOpen().OpenThread().start()
            return
        }

        val cv = ContentValues()

        for (json in jsonList) {
            cv.put("id", json.getString("id"))
            cv.put("name", json.getString("name"))
            cv.put("address", json.getString("address"))
        }

        db.insert("Person", null, cv)

        while (cursor.moveToNext()) {
            val idxPos = cursor.getColumnIndex("id")
            val namePos = cursor.getColumnIndex("name")
            val addressPos = cursor.getColumnIndex("address")

            val idx = cursor.getInt(idxPos)
            val name = cursor.getString(namePos)
            val address = cursor.getString(addressPos)

            val result = "id : ${idx}\n name : ${name}\n address : ${address}\n"
            textView.text = result
        }
    }

    fun dataInsert() {

    }

    fun dataUpdate() {

    }

    fun dataDelete() {

    }

    fun mirrorData() {

    }


    inner class DataOpen() {
        fun open() {
            val thread = OpenThread()
            thread.start()
        }

        inner class OpenThread : Thread() {
            override fun run() {
                Log.d("TEST","Thread start")
                val client = OkHttpClient()
                val url = Request.Builder().url("http://13.125.170.17/ConnectTest.php")
                val request = url.build()

                client.newCall(request).enqueue(OpenCallback())
            }
        }

        inner class OpenCallback : Callback {
            override fun onFailure(call: Call, e: IOException) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("TEST","Connect Success")

                val result = response.body()?.string()
                val arr = JSONArray(result)

                for (i in 0..(arr.length() - 1)) {
                    val obj = arr.getJSONObject(i)
                    jsonList.add(obj)
                }

            }
        }
    }
}



