package com.example.httpservice

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import okhttp3.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.singleTop
import org.jetbrains.anko.toast
import java.io.IOException

class HttpService : Service() {

    var userId : String? = null
    var userPw : String? = null
    private val binder = HttpBindClass()

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("test", "Service onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("test","Service onDestroy")
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        object : Thread() {
            override fun run() {
                val client = OkHttpClient()
                val request = Request.Builder().url("http://13.125.170.17/loginApp.php").apply {
                    post(
                        FormBody.Builder().apply {
                            add("userId", intent!!.getStringExtra("id"))
                            add("userPw", intent.getStringExtra("pw"))
                        }.build()
                    ).build()
                }.build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        runOnUiThread {
                            toast("연결 실패")
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        Log.d("test", "연결 성공")
                        val result = response.body()?.string()

                        when(result){
                            "SUCCESS_LOGIN" ->{
                                Log.d("test","when entry")
                                userId = intent!!.getStringExtra("id")
                                userPw = intent.getStringExtra("pw")
                            }
                        }

                        startActivity(
                            intentFor<MainActivity>(
                                "result" to result
                            ).singleTop()
                        )
                    }
                })
            }
        }.start()

        return super.onStartCommand(intent, flags, startId)
    }

    inner class HttpBindClass : Binder(){
        fun getService() : HttpService = this@HttpService
    }
}
