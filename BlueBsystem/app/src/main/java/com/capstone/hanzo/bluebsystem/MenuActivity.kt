package com.capstone.hanzo.bluebsystem

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.util.Log
import android.widget.Toast
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kr.co.bootpay.Bootpay
import kr.co.bootpay.BootpayAnalytics
import okhttp3.*
import org.altbeacon.beacon.BeaconConsumer
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.jetbrains.anko.appcompat.v7.Appcompat
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.dropTable
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jetbrains.anko.toast
import java.io.IOException
import java.net.URL
import kotlin.system.exitProcess

class MenuActivity : AppCompatActivity() {


    private val tapIconList = intArrayOf(R.drawable.tab_reserve, R.drawable.tab_payment, R.drawable.tab_usermanagement)

    lateinit var beaconManager: BeaconManager
    lateinit var sharedPlatformId: String
    lateinit var sharedPlatformName: String
    lateinit var sharedUserBalance: String
    lateinit var sharedLastBusNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        BootpayAnalytics.init(this, "5c6e1824b6d49c7cbc505f9c")

        /**
         * 비콘 매니저 생성
         * iBeacon을 받기위한 설정
         */
//        beaconManager = BeaconManager.getInstanceForApplication(this).apply {
//            beaconParsers.add(BeaconParser().setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"))
//            bind(this@MenuActivity)
//        }

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

}