package com.capstone.hanzo.bluebsystem

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Window
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.*
import org.jetbrains.anko.*
import org.jetbrains.anko.db.insert
import java.io.IOException
import java.io.InputStream
import java.net.URL

class LoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_loading)

        infoInit()
        loading()
    }

    private fun loading() {
        Handler().postDelayed({
            startActivity(intentFor<LoginActivity>())
            finish()
        }, 500)
    }

    private fun infoInit() = runBlocking {
        val client = OkHttpClient()
        var request: Request

        fun makeRequest(url: String) = Request.Builder().url(url).build()

        val job = launch {
            request = makeRequest(BusNoList.URL)
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        toast("인터넷 연결이 없습니다.").show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    val list = BusNoList.parseJSON(response)
                    database.use {
                        list.forEach {
                            insert(
                                BusNoList.TABLE_NAME,
                                BusNoList.COLUMN_ID to it.busId,
                                BusNoList.COLUMN_NUM to it.busNo,
                                BusNoList.COLUMN_UUID to it.uuid,
                                BusNoList.COLUMN_MAJOR to it.major,
                                BusNoList.COLUMN_START to it.start,
                                BusNoList.COLUMN_END to it.end
                            )
                        }
                    }
                }
            })
        }

        launch {
            job.join()

            request = makeRequest(PlatformArvlInfoList.URL)
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        toast("인터넷 연결이 없습니다").show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    val list = PlatformArvlInfoList.parseJSON(response)
                    database.use {
                        list.forEach {
                            insert(PlatformArvlInfoList.TABLE_NAME,
                                PlatformArvlInfoList.COLUMN_ID to it._id,
                                PlatformArvlInfoList.COLUMN_NUM to it._number,
                                PlatformArvlInfoList.COLUMN_NAME to it._name
                            )
                        }
                    }
                }
            })
        }
    }

    override fun onBackPressed() {
        // 뒤로가기 막음
    }
}


//OkHttpClient().newCall(request).enqueue(object : Callback {
//    override fun onFailure(call: Call, e: IOException) {
//
//    }
//
//    override fun onResponse(call: Call, response: Response) {
//
//        val stream = response.body()?.byteStream()
//        val items: ArrayList<BusNoList> = mapper.readValue(stream!!)
//
//        database.use {
//            items.forEach {
//                insert(
//                    BusNoList.TABLE_NAME,
//                    BusNoList.COLUMN_ID to it.busId,
//                    BusNoList.COLUMN_NUM to it.busNo,
//                    BusNoList.COLUMN_UUID to it.uuid,
//                    BusNoList.COLUMN_MAJOR to it.major,
//                    BusNoList.COLUMN_START to it.start,
//                    BusNoList.COLUMN_END to it.end
//                )
//            }
//        }
//    }
//})

//OkHttpClient().newCall(request).enqueue(object : Callback {
//    override fun onFailure(call: Call, e: IOException) {
//
//    }
//
//    override fun onResponse(call: Call, response: Response) {
//        val stream = response.body()?.byteStream()
//        val items: ArrayList<PlatformArvlInfoList> = mapper.readValue(stream!!)
//        database.use {
//            items.forEach {
//                insert(
//                    PlatformArvlInfoList.TABLE_NAME,
//                    PlatformArvlInfoList.COLUMN_ID to it.platId,
//                    PlatformArvlInfoList.COLUMN_NAME to it.platName,
//                    PlatformArvlInfoList.COLUMN_NUM to it.platNo
//                )
//            }
//        }
//    }
//})