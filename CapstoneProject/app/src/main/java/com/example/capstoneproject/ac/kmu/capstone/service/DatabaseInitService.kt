package com.example.capstoneproject.ac.kmu.capstone.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.capstoneproject.ac.kmu.capstone.activity.BusNoList
import com.example.capstoneproject.ac.kmu.capstone.dbhelper.database
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.*
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.IOException

class DatabaseInitService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        doAsync {
            val request = Request.Builder().url("http://13.125.170.17/busInfoSearch.php").build()

            OkHttpClient().newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                }

                override fun onResponse(call: Call, response: Response) {
                    val result = response.body()?.byteStream()
                    val mapper = jacksonObjectMapper()
                    val list = mapper.readValue<ArrayList<BusNoList>>(result!!)

                    uiThread {

                        list.forEach {
                            database.use {
                                insert(
                                    "BusNoTBL",
                                    "bus_id" to it.busId,
                                    "bus_num" to it.busNo,
                                    "uuid" to it.uuid,
                                    "major" to it.major,
                                    "start_plat" to it.start,
                                    "end_plat" to it.end
                                )
                            }
                        }
                    }
                }
            })
        }
        return super.onStartCommand(intent, flags, startId)
    }
}
