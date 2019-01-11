package ch9294.kr.ac.kmu.activityex1

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SimpleAdapter
import kotlinx.android.synthetic.main.activity_data.*

class DataActivity : AppCompatActivity() {

//    val ids = arrayOf("cci", "kbc", "bym", "hij", "phj")
//    val names = arrayOf("최천일", "김병철", "변용민", "황인정", "박현정")
//    val sex = arrayOf("male", "male", "male", "female", "female")
//    val tel = arrayOf("1111-2222", "2222-3333", "3333-4444", "4444-5555", "5555-6666")

    val keys = arrayOf("id", "name", "age", "sex", "tel")
    val viewId = intArrayOf(R.id._id, R.id._name, R.id._age, R.id._sex, R.id._tel)
    var list = ArrayList<Map<String, String?>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

        var info: MemberInfo = intent.getParcelableExtra("info")
//        Log.d("infomation!!!", "${info._id} ,${info._name}, ${info._age}, ${info._sex}, ${info._tel}")
        var map = mapOf<String, String?>(
            "id" to info._id,
            "name" to info._name,
            "age" to info._age,
            "sex" to info._sex,
            "tel" to info._tel
        )

        list.add(map)

        val adapter = SimpleAdapter(this, list, R.layout.row_shape, keys, viewId)
        dataList.adapter = adapter

        closeBtn.setOnClickListener { v ->
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
