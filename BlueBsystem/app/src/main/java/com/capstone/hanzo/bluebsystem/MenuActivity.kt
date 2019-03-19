package com.capstone.hanzo.bluebsystem

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_menu.*
import kr.co.bootpay.Bootpay
import kr.co.bootpay.BootpayAnalytics
import okhttp3.*
import org.jetbrains.anko.support.v4.runOnUiThread
import java.io.IOException
import java.net.URL

class MenuActivity : AppCompatActivity() {

    private val tapIconList = intArrayOf(R.drawable.tab_reserve, R.drawable.tab_payment, R.drawable.tab_usermanagement)

    lateinit var sharedPlatformId: String
    lateinit var sharedPlatformName: String
    lateinit var sharedUserBalance: String
    lateinit var sharedLastBusNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        BootpayAnalytics.init(this, "5c6e1824b6d49c7cbc505f9c")

        tabLayout.apply {
            tapIconList.forEach {
                addTab(this.newTab().setIcon(it))
            }
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        pager.currentItem = it.position
                    }
                }
                override fun onTabReselected(p0: TabLayout.Tab?) {
                }

                override fun onTabUnselected(p0: TabLayout.Tab?) {
                }
            })
            tabGravity = TabLayout.GRAVITY_FILL
        }

        pager.apply {
            adapter = TapPagerAdapter(supportFragmentManager)
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        }
    }

    override fun onResume() {
        super.onResume()
    }


}