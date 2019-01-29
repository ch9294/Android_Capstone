package com.example.ipc

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.SystemClock
import android.util.Log

class IPCService : Service() {

    var value = 0
    var thread: Thread? = null
    private val binder: IBinder = LocalBinder()

    override fun onCreate() {
        super.onCreate()

        Log.d("test1", "Service onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("test1","Service onDestroy")
    }
    /*
    bindService() 메소드를 호출하면 실행되는 콜백 메소드
     */
    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    /*
    startService() 메소드를 호출하면 실행되는 콜백 메소드
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d("test", "Service onStartCommand")
        thread = ThreadClass()
        thread?.start()
        return super.onStartCommand(intent, flags, startId)
    }

    inner class ThreadClass : Thread() {
        override fun run() {
            Log.d("test1", "Thread Start")
            while (true) {
                SystemClock.sleep(1000)
                Log.d("test1", "value : ${value}")
                value++
            }
        }
    }

    inner class LocalBinder : Binder() {
        fun getService(): IPCService {
            return this@IPCService
        }
    }

    fun getNumber(): Int {
        return value
    }
}
