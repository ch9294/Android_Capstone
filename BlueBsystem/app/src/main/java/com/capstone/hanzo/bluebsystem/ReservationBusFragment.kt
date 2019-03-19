package com.capstone.hanzo.bluebsystem


import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.*
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.*
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

        /**
         * 각각의 뷰들의 설정
         */
        RB_searchST.run {
            hint = "노선번호 검색"
        }

        PlatformListInitThread().start()
        NumberListInitThread().start()

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

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                        }

                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                        }
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

                            controller.sharedPlatformId = v.platId
                            controller.sharedPlatformName = v.platName

                            trans.apply {
                                replace(R.id.mainContainer, PlatformInfoFragment())
                                hide(this@ReservationBusFragment)
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
                items.forEach {
                    platAdapter.addItem(it.platId, it.platName, it.platNo)
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
                    busAdapter.addItem(it.busId, it.busNo, it.uuid, it.major, it.start, it.end)
                }
            }
        }
    }

}
