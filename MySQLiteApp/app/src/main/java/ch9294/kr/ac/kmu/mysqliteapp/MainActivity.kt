package ch9294.kr.ac.kmu.mysqliteapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val controller = DataController(DataHelper(this).writableDatabase, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 검색하기
        button.setOnClickListener { v ->
            controller.dataSelect()
        }

        // 삽입하기
        button2.setOnClickListener { v ->

        }

        // 삭제하기
        button3.setOnClickListener { v ->

        }

        // 수정하기
        button4.setOnClickListener { v ->

        }

        // 서버의 데이터를 보여줌
        button5.setOnClickListener { v ->

        }

        // 내부 데이터베이스의 변경사항을 서버의 데이터베이스로 적용
        button6.setOnClickListener { v ->

        }
    }
}
