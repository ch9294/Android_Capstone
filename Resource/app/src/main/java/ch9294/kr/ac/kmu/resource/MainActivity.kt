package ch9294.kr.ac.kmu.resource

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener { v ->
            //            textView.text = resources.getText(R.string.str2)

            // textView에서만 제공하는 기능이다.
            // setText 메소드를 이용해 리소스 파일을 바로 접근
            textView.setText(R.string.str2)
        }

        button4.setOnClickListener { v ->
            val arr = resources.getStringArray(R.array.data_array)
            textView.text = ""

            for (str in arr) {
                textView.append("${str}\n")
            }
        }

        button5.setOnClickListener { v ->

            //            textView.setTextColor(Color.GREEN)

            /*
            개발자 본인이 색상을 만들기
             */
//            var color = Color.rgb(26, 106, 129)

            /*
            투명도 설정 포함하기
             */
//            var color = Color.argb(50, 26, 106, 129)

            val color = ContextCompat.getColor(this, R.color.color1)
            textView.setTextColor(color)

        }

        button6.setOnClickListener { v ->
            val px = resources.getDimension(R.dimen.px)
            val dp = resources.getDimension(R.dimen.dp)
            val inch = resources.getDimension(R.dimen.inch)
            val sp = resources.getDimension(R.dimen.sp)
            val mm = resources.getDimension(R.dimen.mm)
            val pt = resources.getDimension(R.dimen.pt)

            textView.text = "px : ${px}\n"
            textView.append("dp : ${dp}\n")
            textView.append("sp : ${sp}\n")
            textView.append("inch : ${inch}\n")
            textView.append("mm : ${mm}\n")
            textView.append("pt : ${pt}\n")

            textView.setTextSize(20 * dp)
        }
    }
}
