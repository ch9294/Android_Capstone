package com.capstone.hanzo.altbeacontest

import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.altbeacon.beacon.*
import java.lang.Exception
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

class MainActivity : AppCompatActivity(), BeaconConsumer {

    private val permissions = arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION)
    private lateinit var beaconManager: BeaconManager

    companion object {
        const val TAG = "PERMISSION"
        const val BEACON_TAG = "Beacon"
        const val PERMISSION = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermission()

        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),1)

        beaconManager = BeaconManager.getInstanceForApplication(this).apply {
            beaconParsers.add(BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"))

        }
        beaconManager.bind(this);
    }

    override fun onDestroy() {
        beaconManager.unbind(this)
        super.onDestroy()
    }

    override fun onBeaconServiceConnect() {
        Log.d(BEACON_TAG, "onBeaconServiceConnect() Call")
        beaconManager.run {
            removeAllRangeNotifiers()
            addRangeNotifier { beacons, region ->
                when (beacons.isNotEmpty()) {
                    true -> {
                        Log.d(BEACON_TAG, "beacons is not empty")
                        beacons.forEach {

                            val df = DecimalFormat("#.##").apply { roundingMode = RoundingMode.CEILING }
                            textView.run {
                                if (it.distance <= 0.5) {
                                    Log.d(BEACON_TAG, "${df.format(it.distance)}m, 1.5m 이내 감지")
                                }else{
                                    Log.d(BEACON_TAG, "${df.format(it.distance)}m")
                                }
                            }
                        }
                    }
                    false -> {
                        Log.d(BEACON_TAG, "beacons is empty")
                    }
                }
            }
            try {
                startRangingBeaconsInRegion(Region("MyBeacon", null, null, null))
            } catch (e: Exception) {
                Log.d(BEACON_TAG, e.stackTrace.toString())
            }
        }
    }


    private fun requestPermission() {
        Log.d(TAG, "requestPermission() Call ")

        if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION -> {
                if (grantResults.isEmpty()) {
                    Toast.makeText(this, "위치 권한이 거부되었습니다", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "위치 권한이 허되었습니다", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

