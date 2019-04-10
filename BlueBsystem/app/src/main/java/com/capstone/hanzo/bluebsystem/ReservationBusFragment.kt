package com.capstone.hanzo.bluebsystem


import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import org.jetbrains.anko.*
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.*
import org.jetbrains.anko.db.RowParser
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.sdk27.coroutines.onItemClick
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jetbrains.anko.support.v4.toast
import java.io.IOException


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */

class ReservationBusFragment : Fragment(), AnkoLogger {
    private lateinit var RB_searchST: EditText
    private lateinit var RB_numSearch: TextView
    private lateinit var RB_stSearch: TextView
    private lateinit var RB_listView: ListView
    private val platAdapter = PlatformListAdapter()
    private val busAdapter = NumberListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_reservation_bus, container, false).apply {
            RB_searchST = find(R.id.RB_searchST)
            RB_numSearch = find(R.id.RB_numSearch)
            RB_stSearch = find(R.id.RB_stSearch)
            RB_listView = find(R.id.RB_listView)
        }

        // 액티비티 컨트롤러
        val controller = activity as MenuActivity

        val imm = with(controller) {
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        }

        displayInfo()
//        PlatformListInitThread().start()
//        NumberListInitThread().start()

        /**
         * 각각의 뷰들의 설정
         */
        RB_searchST.run {
            hint = "노선번호 검색"
        }

        RB_numSearch.run {
            setTextColor(Color.parseColor("#FFFFFF"))
            setBackgroundColor(Color.parseColor("#000E2B"))
            setOnClickListener {

                RB_searchST.run {
                    setText("")
                    hint = "노선번호 검색"
                    setOnKeyListener { v, keyCode, event ->
                        when (keyCode) {
                            KeyEvent.KEYCODE_ENTER -> imm.hideSoftInputFromWindow(v.windowToken, 0)
                            else -> false
                        }
                    }
                    addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {
                            val filterText = s.toString()
                            if (filterText.isEmpty()) {
                                RB_listView.clearTextFilter()
                            } else {
                                RB_listView.setFilterText(filterText)
                            }
                        }

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        }

                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        }
                    })
                }

                RB_numSearch.run {
                    setTextColor(Color.parseColor("#FFFFFF"))
                    setBackgroundColor(Color.parseColor("#000E2B"))
                }

                RB_stSearch.run {
                    setTextColor(Color.parseColor("#000E2B"))
                    setBackgroundColor(Color.parseColor("#FFFFFF"))
                }

                RB_listView.run {
                    adapter = busAdapter
                    onItemClickListener = object : AdapterView.OnItemClickListener {
                        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            val v = parent?.getItemAtPosition(position) as BusNoList
                        }
                    }
                }
            }

        }

        RB_stSearch.run {
            setOnClickListener {
                RB_searchST.run {
                    setText("")
                    hint = "정류장 검색"
                    setOnKeyListener { v, keyCode, event ->
                        when (keyCode) {
                            KeyEvent.KEYCODE_ENTER -> imm.hideSoftInputFromWindow(v.windowToken, 0)
                            else -> false
                        }
                    }
                    addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {
                            val filterText = s.toString()
                            if (filterText.isEmpty()) {
                                RB_listView.clearTextFilter()
                            } else {
                                RB_listView.setFilterText(filterText)
                            }
                        }

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    })
                }


                RB_stSearch.run {
                    setTextColor(Color.parseColor("#FFFFFF"))
                    setBackgroundColor(Color.parseColor("#000E2B"))
                }

                RB_numSearch.run {
                    setTextColor(Color.parseColor("#000E2B"))
                    setBackgroundColor(Color.parseColor("#FFFFFF"))
                }

                RB_listView.run {
                    adapter = platAdapter
                    onItemClickListener = object : AdapterView.OnItemClickListener {
                        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            val trans = controller.supportFragmentManager.beginTransaction()
                            val v = parent?.getItemAtPosition(position) as PlatformArvlInfoList

                            /*
                            내부 데이터베이스 사용때에는 정류장 번호와 이름이 뒤바뀌어 저장됨.(이유는 알 수없음 ㅜㅜ)
                             */
                            controller.run {
                                sharedPlatformId = v._id
                                sharedPlatformName = v._number
                            }

                            trans.apply {
                                add(R.id.mainContainer, PlatformInfoFragment())
//                                hide(this@ReservationBusFragment)
                                addToBackStack(null)
                            }.commit()
                        }
                    }
                }
            }
        }

        RB_listView.apply {
            isTextFilterEnabled = true
        }

        return view
    }

    // 정류장 리스트 초기화 쓰레드
    inner class PlatformListInitThread : Thread() {
        override fun run() {
            val request = Request.Builder().url("http://13.125.170.17/platformInfoSearch.php").build()
            OkHttpClient().newCall(request).enqueue(PlatformListInitCallback())
        }
    }

    // 정류장 리스트를 초기화하는 서버의 응답
    inner class PlatformListInitCallback : Callback {
        override fun onFailure(call: Call, e: IOException) {
            runOnUiThread {
                toast("접속 실패")
            }
        }

        override fun onResponse(call: Call, response: Response) {
            val stream = response.body()?.byteStream()
            val mapper = jacksonObjectMapper()
            val items: ArrayList<PlatformArvlInfoList> = mapper.readValue(stream!!)

            runOnUiThread {
                // 정류장 이름을 사전순으로 정렬
                items.sortBy { it._name }
                for (item in items) {
                    platAdapter.addItem(item)
                }
            }
        }
    }

    // 버스 번호 리스트 초기화 쓰레드
    inner class NumberListInitThread : Thread() {
        override fun run() {
            val request = Request.Builder().url("http://13.125.170.17/busInfoSearch.php").build()
            OkHttpClient().newCall(request).enqueue(NumberListInitCallback())
        }
    }

    // 버스 번호 리스트를 초기화하는 서버의 응답
    inner class NumberListInitCallback : Callback {
        override fun onFailure(call: Call, e: IOException) {
            runOnUiThread {
                toast("접속 실패")
            }
        }

        override fun onResponse(call: Call, response: Response) {
            val stream = response.body()?.byteStream()
            val mapper = jacksonObjectMapper()
            val items: ArrayList<BusNoList> = mapper.readValue(stream!!)

            runOnUiThread {
                items.forEach {
                    busAdapter.addItem(BusNoList(it.busId, it.busNo, it.uuid, it.major, it.start, it.end))
                }
            }
        }
    }

    fun displayInfo() = runBlocking {
        (activity as MenuActivity).database.use {

            select(BusNoList.TABLE_NAME).parseList(classParser<BusNoList>()).forEach {
                busAdapter.addItem(BusNoList(it.busId, it.busNo, it.uuid, it.major, it.start, it.end))
            }

            /**
             * Anko select 확장 함수를 사용 할때에 알 수 없는 이유로 정류장 이름과 정류장 번호가 서로 바뀌어서 나옴
             * _name은 번호, _number = 이름 으로 생각하고 진행
             *
             * 내부 데이터베이스에 저장된 데이터는 올바르게 저장되어있음!! (2019.04.02)
             */
            select(PlatformArvlInfoList.TABLE_NAME).parseList(classParser<PlatformArvlInfoList>())
                .filter { it._name != "no_search" }
                .forEach { platAdapter.addItem(PlatformArvlInfoList(it._id, it._number, it._name)) }
        }
        RB_listView.adapter = busAdapter
    }
}


//AlertDialog.Builder(activity as MenuActivity).apply {
//    setTitle("${v.busNo}번 버스 예약하기")
//    setView(controller.layoutInflater.inflate(R.layout.reservation_alert, null).apply {
//        val expense: TextView = find(R.id.expenseAlert)
//        val balance: TextView = find(R.id.balanceAlert)
//        val msg: TextView = find(R.id.msgAlert)
//
//        expense.run {
//            when (v.busNo.contains("급행")) {
//                true -> text = 1650.toString()
//                false -> text = 1250.toString()
//            }
//        }
//
//        balance.run {
//            text = controller.sharedUserBalance
//        }
//
//        msg.run {
//            when (expense.text.toString().toInt() > balance.text.toString().toInt()) {
//                true -> {
//                    setTextColor(Color.RED)
//                    text = "잔액이 부족합니다"
//                }
//
//                false -> {
//                    setTextColor(Color.BLUE)
//                    text = "예약이 가능합니다"
//                }
//            }
//        }
//    })
//
//    val listener = object : DialogInterface.OnClickListener {
//        override fun onClick(dialog: DialogInterface?, which: Int) {
//
//        }
//    }
//    setPositiveButton("예약", listener)
//    setNegativeButton("취소", listener)
//}.show()