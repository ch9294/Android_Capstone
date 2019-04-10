package com.capstone.hanzo.coroutine1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(),Beacon {


    companion object {
        const val TAG = "코루틴 돌리기"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent1 = Intent(this, TestActivity1::class.java)
        val pending1 = PendingIntent.getActivity(this, 0, intent1, 0)

        val intent2 = Intent(this, TestActivity2::class.java)
        val pending2 = PendingIntent.getActivity(this, 0, intent2, 0)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        btn1.setOnClickListener {
            val builder = getNotificationBuilder("pending", "pending intent").apply {
                setContentTitle("알림 메세지 1입니다")
                setContentText("notification 1")
                setSmallIcon(android.R.drawable.ic_menu_camera)
                setAutoCancel(true) // 메세지 누를 시 자동으로 없애주는 것
                setContentIntent(pending1) // pending intent 설정하기
            }

            val notification = builder.build()
            manager.notify(10, notification)
        }

        btn2.setOnClickListener {
            val builder = getNotificationBuilder("pending", "pending intent").apply {
                setContentTitle("알림 메세지 2입니다")
                setContentText("notification 2")
                setSmallIcon(android.R.drawable.ic_menu_camera)
                setAutoCancel(true) // 메세지 누를 시 자동으로 없애주는 것
                setContentIntent(pending2) // pending intent 설정하기
            }

            val notification = builder.build()
            manager.notify(10, notification)
        }
    }

    private fun getNotificationBuilder(id: String, name: String): NotificationCompat.Builder {
        var builder: NotificationCompat.Builder? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
            channel.run {
                enableLights(true)
                lightColor = Color.RED
            }
            manager.createNotificationChannel(channel)

            builder = NotificationCompat.Builder(this, id)
        } else {
            builder = NotificationCompat.Builder(this)
        }
        return builder
    }
}
