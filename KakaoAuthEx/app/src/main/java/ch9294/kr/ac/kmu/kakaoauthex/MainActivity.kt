package ch9294.kr.ac.kmu.kakaoauthex

import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import java.lang.Exception
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getHashKey()
    }

    fun getHashKey() {
        try {
            val info = packageManager.getPackageInfo("ch9294.kr.ac.kmu.kakaoauthex", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("hashKey", "key_hash = ${Base64.encodeToString(md.digest(), Base64.DEFAULT)}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
