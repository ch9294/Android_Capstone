package com.example.capstoneproject.ac.kmu.capstone.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.capstoneproject.R
import com.example.capstoneproject.ac.kmu.capstone.activity.HomeActivity
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.android.synthetic.main.fragment_search.*
import okhttp3.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.alert
import java.io.IOException
import java.net.URL


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class SearchFragment : Fragment(), AnkoLogger {

    val busKeys = arrayOf("busNum", "start", "end")
    val busIds = intArrayOf(R.id.textView, R.id.textView2, R.id.textView3)

    val platKeys = arrayOf("platName", "platId")
    val platIds = intArrayOf(R.id.platName, R.id.platId)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_search, container, false)

        // 검색 프래그먼트의 액티비티 컨트롤러
        val controller = activity as HomeActivity

        /**
         *  xml의 뷰들을 객체화
         */
        val searchView = view.find<android.support.v7.widget.SearchView>(R.id.searchView)
        val searchGrp = view.find<RadioGroup>(R.id.searchGrp)
        val searchList = view.find<ListView>(R.id.searchList)

        // 현재 프래그먼트를 관리하는 컨트롤러가 가진 리스트 변수에 리스트를 만들어 할당한다
        controller.busNoList = makeBusList()
        controller.platformList = makePlatformList()

        // 각각의 어댑터를 설정한다
        val busAdapter = SimpleAdapter(controller, controller.busNoList, R.layout.bus_no_listview, busKeys, busIds)
        val platformAdapter = SimpleAdapter(controller, controller.platformList, R.layout.platfrom_listview, platKeys, platIds)

        /**
         * 라디오 버튼의 선택에 따라 리스트뷰가 보여줄 목록을 변경한다.
         */
        searchGrp.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioBus -> {
                    searchView.queryHint = "버스 번호를 입력해주세요"
                    searchList.adapter = busAdapter
                }
                R.id.radioPlatform -> {
                    searchView.queryHint = "정류장 이름을 입력해주세요"
                    searchList.adapter = platformAdapter
                }
            }
        }

        // 리스트뷰의 검색 기능을 활성화한다
        searchList.isTextFilterEnabled = true

        // 서치뷰에 텍스트가 입력될때의 이벤트를 설정한다
        searchView.setOnQueryTextListener(QueryListener(searchList))
        return view
    }


    /***
     * 버스 목록을 초기화한다.
     */
    private fun makeBusList(): ArrayList<Map<String, String>> {
        val arrList = ArrayList<Map<String, String>>()
        var list: ArrayList<BusNoList>

        doAsync {
            val stream = URL("http://13.125.170.17/busInfoSearch.php").openStream()
            val mapper = jacksonObjectMapper()
            list = mapper.readValue<ArrayList<BusNoList>>(stream!!)

            uiThread {
                list.forEach {
                    val map = mapOf(
                        "busId" to it.busId,
                        "busNum" to it.busNo,
                        "uuid" to it.uuid,
                        "major" to it.major,
                        "start" to it.start,
                        "end" to it.end
                    )
                    arrList.add(map)
                }
            }
        }
        return arrList
    }

    /***
     * 정류장 목록을 초기화한다.
     */
    private fun makePlatformList(): ArrayList<Map<String, String>> {
        val arrList = ArrayList<Map<String, String>>()
        var list: ArrayList<PlatformArvlInfoList>

        doAsync {
            val stream = URL("http://13.125.170.17/platformInfoSearch.php").openStream()
            val mapper = jacksonObjectMapper()
            list = mapper.readValue(stream!!)

            uiThread {
                list.forEach {
                    val map = mapOf(
                        "platId" to it.platId,
                        "platName" to it.platName
                    )
                    arrList.add(map)
                }
            }
        }
        return arrList
    }

    inner class QueryListener(val listView: ListView) : android.support.v7.widget.SearchView.OnQueryTextListener {
        override fun onQueryTextChange(newText: String?): Boolean {
            listView.setFilterText(newText)
            if (newText?.length == 0) {
                listView.clearTextFilter()
            }
            return true
        }

        override fun onQueryTextSubmit(p0: String?): Boolean {
            searchView.clearFocus()
            return true
        }
    }

    inner class BusItemClickListener : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        }
    }

    inner class PlatformItemClickListener : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        }
    }
}


data class BusNoList(
    val busId: String,
    val busNo: String,
    val uuid: String,
    val major: String,
    val start: String,
    val end: String
)


data class PlatformArvlInfoList(val platId: String, val platName: String)