package ch9294.kr.ac.kmu.activityapp1

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val SECOND_ACTIVITY = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener { v ->
            /*
            다른 어플리케이션의 액티비티를 실행하고자 한다면 해당 액티비티가 자신의 매니페스트 파일에서
            Intent filter가 지정되어 있어야 한다.
            action에서 name 속성으로 자신이 원하는 이름을 지으면 된다.
            category는 default로 지정함
             */
            val intent = Intent("com.test.second")
            intent.putExtra("data1", 100)
            intent.putExtra("data2", 11.11)
            startActivityForResult(intent, SECOND_ACTIVITY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var value1 = data?.getIntExtra("value1", 0)
        var value2 = data?.getDoubleExtra("value2", 0.0)
        var value3 = data?.getStringExtra("value3")
    }
}
