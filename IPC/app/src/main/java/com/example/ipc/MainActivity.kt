package com.example.ipc

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var ipcService: IPCService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, IPCService::class.java)
        if (!isServiceRunning("com.example.ipc.IPCService")) {
            startService(intent)
        }

        /*
          액티비티가 서비스로부터 데이터를 받아오려고 할때 서비스가 켜져있지 않으면
          실행시키고 데이터를 가져온다.
         */
        bindService(
            intent,
            mConnection,
            Context.BIND_AUTO_CREATE
        )

        button.setOnClickListener {
            val value = ipcService?.getNumber()
            textView.text = "value : ${value}"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(mConnection)
    }

    private fun isServiceRunning(name: String): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        for (service: ActivityManager.RunningServiceInfo in manager.getRunningServices(Int.MAX_VALUE)) {
            if (service.service.className.equals(name)) {
                return true
            }
        }
        return false
    }

    /*
    실행 중인 서비스로부터 데이터를 받아오기 위해 필요한 서비스 커넥션이다.
    bindService() 메소드에 인자로 넣게 되면 실행 중인 서비스의 onBind() 메소드가 호출되면서
    Binder 객체를 커넥션으로 넘겨주게 된다.
     */
    private val mConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {

        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as IPCService.LocalBinder
            ipcService = binder.getService()
        }
    }
}
