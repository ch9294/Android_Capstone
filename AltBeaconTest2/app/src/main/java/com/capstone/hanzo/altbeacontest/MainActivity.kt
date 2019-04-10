package com.capstone.hanzo.altbeacontest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.altbeacon.beacon.*

class MainActivity : AppCompatActivity(), BeaconConsumer {

    companion object {
        const val TAG = "비콘테스트"
    }

    private lateinit var beaconManager: BeaconManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        beaconManager = BeaconManager.getInstanceForApplication(this).apply {
            beaconParsers.add(BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"))
        }
        beaconManager.bind(this)
    }

    override fun onBeaconServiceConnect() {
        Log.d(TAG, "onBeaconServiceConnect 호출")
        startRangingBeacon()

    }

//    private fun monitorBeacon() = beaconManager.run {
//        removeAllMonitorNotifiers()
//        addMonitorNotifier(object : MonitorNotifier {
//            override fun didDetermineStateForRegion(p0: Int, p1: Region?) {
//            }
//
//            override fun didEnterRegion(p0: Region?) {
//                Log.d(TAG, "비콘 발견")
//                textView.text = "비콘 발견"
//                startRangingBeacon()
//            }
//
//            override fun didExitRegion(p0: Region?) {
//                Log.d(TAG, "비콘 수신 중단")
//                textView.text = "비콘 없음"
//            }
//        })
//
//        try {
//            startMonitoringBeaconsInRegion(Region("myRegion", null, null, null))
//        } catch (e: Exception) {
//
//        }
//    }

    private fun startRangingBeacon() = beaconManager.run {
            Log.d(TAG, "startRangingBeacon 호출")
            var cnt: Int = 0
            removeAllRangeNotifiers()
            addRangeNotifier { collection, region ->
                if (cnt == 5) {
                    return@addRangeNotifier
                }
                if (collection.isNotEmpty()) {
                    cnt = 0
                    collection.forEach {
                        textView3.text = "${it.id1}"
                    }
                } else {
                    cnt++
                    textView2.text = "없음, cnt : $cnt"
                    textView3.text = ""
                }
            }
            try {
                startRangingBeaconsInRegion(Region("myRegion", null, null, null))
            } catch (e: Exception) {
            }
        }

}
