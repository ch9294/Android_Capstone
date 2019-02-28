package com.example.capstoneproject.ac.kmu.capstone.activity

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.capstoneproject.*
import com.example.capstoneproject.ac.kmu.capstone.fragment.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*
import kr.co.bootpay.BootpayAnalytics
import okhttp3.*
import org.jetbrains.anko.*
import java.io.IOException

class HomeActivity : AppCompatActivity(), AnkoLogger {

    private val fragList = ArrayList<Fragment>()

    /***
     * 버스 목록과 정류장 목록을 현재 액티비티가 관리한다.
     * 이 리스트들은 검색 프래그먼트의 버스 목록과 정류장 목록을 리스트뷰로 나타내기 위한 것이다
     */
    lateinit var busNoList: ArrayList<Map<String, String>>
    lateinit var platformList: ArrayList<Map<String, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(homeTool)


        /**
         * 뷰페이저에 보여줄 프래그먼트의 객체를 생성한다.
         */
        fragList.apply {
            add(SearchFragment())
            add(RechargeFragment())
            add(UserInfoFragment())
        }

        /**
         * 뷰페이저에 어댑터를 연결한다.
         */
        homePager.apply {
            adapter = PageAdapter(supportFragmentManager)
        }

        /**
         * 탭의 초기 설정을 한다.
         */
        tabs.apply {
            setTabTextColors(Color.BLACK, Color.BLUE)
            setupWithViewPager(homePager)
        }

    }


    override fun onResume() {
        super.onResume()
        /**
         * 현재 액티비티가 재시작 될 때 마다 현재 사용자의 잔액을 갱신한다
         */
        doAsync {
            val url = Request.Builder().url("http://13.125.170.17/googleUserSelect.php")
            val body = FormBody.Builder().apply {
                add("user_email", FirebaseAuth.getInstance().currentUser?.email!!)
            }.build()

            val request = url.post(body).build()
            OkHttpClient().newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }
                override fun onResponse(call: Call, response: Response) {
                    val result = response.body()?.string()

                    uiThread {
                        appbar.apply {
                            title = "₩$result"
                        }
                    }
                }
            })
        }
    }
    /**
     * 뷰페이저에 설정할 어댑터를 정의한다.
     */
    inner class PageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getCount(): Int {
            return fragList.size
        }

        override fun getItem(idx: Int): Fragment {
            return fragList[idx]
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return "검색"
                1 -> return "충전"
                2 -> return "사용자 정보"
                else -> return null
            }
        }
    }
}
