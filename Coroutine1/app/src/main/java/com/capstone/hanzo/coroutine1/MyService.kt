package com.capstone.hanzo.coroutine1

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MyService : Service() {

    private var job: Job? = null

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        job = coroutine()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
    private fun coroutine() = CoroutineScope(Dispatchers.IO).launch {
        while (true) {
            delay(1000L)
            Log.d("코루틴 돌리기","시간아 간다!!")
        }
    }
}
