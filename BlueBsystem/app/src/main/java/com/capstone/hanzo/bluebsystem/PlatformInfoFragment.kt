package com.capstone.hanzo.bluebsystem


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import okhttp3.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.find
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jetbrains.anko.support.v4.toast
import org.w3c.dom.Element
import java.io.IOException
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class PlatformInfoFragment : Fragment(), AnkoLogger, SwipeRefreshLayout.OnRefreshListener {
    private lateinit var PI_ListView: ListView
    private lateinit var PI_listPlatName: TextView
    private lateinit var PI_swipe: SwipeRefreshLayout
    private val listAdapter = PlatformArvlInfoListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_platform_info2, container, false)
        with(view) {
            PI_ListView = find(R.id.PI_ListView2)
            PI_listPlatName = find(R.id.PI_listPlatName2)
            PI_swipe = find(R.id.listSwipe)
        }
        PlatFormInfoThread().start()

        PI_swipe.apply {
            setOnRefreshListener(this@PlatformInfoFragment)
        }
        PI_listPlatName.apply {
            text = (activity as MenuActivity).sharedPlatformName
        }

        return view
    }

    override fun onRefresh() {
        PI_ListView.apply {
            postDelayed(Runnable {
                toast("새로고침").show()
                PlatFormInfoThread().start()
            }, 500)
            PI_swipe.isRefreshing = false
        }
    }

    inner class PlatFormInfoThread : Thread() {
        override fun run() {
            val stream = URL(
                "http://openapi.tago.go.kr/openapi/service/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList?" +
                        "ServiceKey=SQYXLo2JYmzB7pvVorqfLma6NF38tdCUkcJ0Pn0pXJC0G4fPu%2F7xt%2Bqpoq%2F1qkiBw1krMnqNMNqxcLs0H3B7%2Bw%3D%3D" +
                        "&cityCode=22" +
                        "&nodeId=${(activity as MenuActivity).sharedPlatformId}" +
                        "&numOfRows=20"
            ).openStream()

            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val doc = builder.parse(stream)
            val root = doc.documentElement

            val itemList = root.getElementsByTagName("item")

            // 어댑터 내의 내용을 모두 제거한다.(리스트뷰에 새로운 내용을 갱신하기 위해)
            listAdapter.clear()

            for (idx in 0 until itemList.length) {
                val node = itemList.item(idx) as Element
                val time =
                    (((node.getElementsByTagName("arrtime").item(0) as Element).textContent).toInt() / 60).toString()
                val busNum = (node.getElementsByTagName("routeno").item(0) as Element).textContent
                val type = (node.getElementsByTagName("vehicletp").item(0) as Element).textContent
                listAdapter.addItem(busNum, time, type)
            }
            runOnUiThread {

                PI_ListView.adapter = listAdapter
            }
        }
    }

}