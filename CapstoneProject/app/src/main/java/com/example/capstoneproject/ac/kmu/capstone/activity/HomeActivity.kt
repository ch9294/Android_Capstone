package com.example.capstoneproject.ac.kmu.capstone.activity

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.capstoneproject.*
import com.example.capstoneproject.ac.kmu.capstone.fragment.RechargeFragment
import com.example.capstoneproject.ac.kmu.capstone.fragment.SearchFragment
import com.example.capstoneproject.ac.kmu.capstone.fragment.UserInfoFragment
import com.example.capstoneproject.ac.kmu.capstone.service.DatabaseInitService
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.*

class HomeActivity : AppCompatActivity(), AnkoLogger {

    private val fragList = ArrayList<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(homeTool)

        fragList.apply {
            add(SearchFragment())
            add(RechargeFragment())
            add(UserInfoFragment())
        }

        homePager.apply {
            adapter = PageAdapter(supportFragmentManager)
        }

        tabs.apply {
            setTabTextColors(Color.BLACK, Color.BLUE)
//            setBackgroundColor(Color.WHITE)
            setupWithViewPager(homePager)
        }

    }

    override fun onStart() {
        super.onStart()
        startService(intentFor<DatabaseInitService>())
    }

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


data class BusNoList(
    val busId: String,
    val busNo: String,
    val uuid: String,
    val major: String,
    val start: String,
    val end: String
)
