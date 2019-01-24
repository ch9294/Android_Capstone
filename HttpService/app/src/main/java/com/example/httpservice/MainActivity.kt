package com.example.httpservice

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.intentFor

class MainActivity : AppCompatActivity() {

    var ipc : HttpService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            startService(
                intentFor<HttpService>(
                    "id" to "ch9294",
                    "pw" to "7269"
                )
            )
        }

        button2.setOnClickListener {
            val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

            for (service in manager.getRunningServices(Int.MAX_VALUE)) {
                if (service.service.className == "com.example.httpservice.HttpService") {
                    bindService(intentFor<HttpService>(), mConnection, Context.BIND_AUTO_CREATE)
                }
            }

            textView2.text = "id : ${ipc?.userId}\n"
            textView2.append("pw : ${ipc?.userPw}\n")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(mConnection)
    }

    override fun onNewIntent(intent: Intent?) {
        val result = intent!!.getStringExtra("result")
        textView.text = "결과값 : "
        textView.append(result)
//        stopService(intentFor<HttpService>())
    }

    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as HttpService.HttpBindClass
            ipc = binder.getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}
