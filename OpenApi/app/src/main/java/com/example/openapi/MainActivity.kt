package com.example.openapi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.jetbrains.anko.toast
import org.json.JSONObject
import org.w3c.dom.Element
import java.io.IOException
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView.text = "오픈 api 테스트"

        button.setOnClickListener {
            val thread = ApiThread()
            thread.start()
        }
    }

    inner class ApiThread : Thread() {
        private val SERVICE_NAME = "serviceName"
        private val OPERARION = "operation"
        private val SERVICE_KEY = "serviceKey"
        private val CITY_CODE = "cityCode"
        private val CITY_NAME = "nodeId"
        private val RETURN_TYPE = "_type"

        override fun run() {
            val operation = "getCtyCodeList"
            val serviceName = "BusLcInfoInqireService/${operation}"
            val serviceKey =
                "SQYXLo2JYmzB7pvVorqfLma6NF38tdCUkcJ0Pn0pXJC0G4fPu%2F7xt%2Bqpoq%2F1qkiBw1krMnqNMNqxcLs0H3B7%2Bw%3D%3D"
            val cityCode = 22.toString()
            val cityName = "대구광역시"

            val address =
                "http://openapi.tago.go.kr/openapi/service/${serviceName}?ServiceKey=${serviceKey}"


            val client = OkHttpClient()
            val url = Request.Builder().url(address)
            val request = url.build()

            client.newCall(request).enqueue(ApiCallback())

        }
    }

    inner class ApiCallback : Callback {
        override fun onFailure(p0: Call, p1: IOException) {
            toast("연결 실패")
        }

        override fun onResponse(p0: Call, p1: Response) {
            Log.d("test", "Http Connect Success")
            val result = p1.body()?.byteStream()

            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val doc = builder.parse(result)

            val root = doc.documentElement
            val item_node_list = root.getElementsByTagName("item")

            for (i in 0 until item_node_list.length) {
                val item_node = item_node_list.item(i) as Element
                val cityCode = item_node.getElementsByTagName("citycode")
                val cityName = item_node.getElementsByTagName("cityname")

                val cityCodeData = cityCode.item(0) as Element
                val cityNameData = cityName.item(0) as Element

                val cityCodeResult = cityCodeData.textContent
                val cityNameResult = cityNameData.textContent

                runOnUiThread {
                    textView.text = "도시코드 : ${cityCodeResult}\n"
                    textView.append("도시이름 : ${cityNameResult}\n\n")
                }
            }

        }

    }
}
