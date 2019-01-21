package ch9294.kr.ac.kmu.beaconsystem

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.style

class CentralActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_central)
    }

    override fun onStart() {
        super.onStart()
        val loginInfo = getPreferences(Context.MODE_PRIVATE)

        if (loginInfo.getString("id", null) != null) {
//            로그인이 된 상태이기 때문에 바로 onCreate를 호출함
        } else {
            UI {
                toast("로그인 상태가 아닙니다")
            }
        }
    }
}

class CentralActivityUI : AnkoComponent<CentralActivity> {
    private lateinit var moneyInfo: TextView

    override fun createView(ui: AnkoContext<CentralActivity>) = with(ui) {
        verticalLayout(R.style.top_layout_setting) {
            linearLayout {
                include<TextView>(R.style.central_textview_setting) {
                    textResource = R.string.textview_money
                }.lparams(weight = 2f)
                include<TextView>(R.style.central_textview_setting) {

                }.lparams(weight = 1f)
            }.lparams(matchParent, wrapContent)

            include<LinearLayout>(R.style.basic_layout_setting) {
                include<Button>(R.style.central_btn_setting) {
                    textResource = R.string.btn_book
                    setOnClickListener {

                    }
                }
            }
            include<LinearLayout>(R.style.basic_layout_setting) {
                include<Button>(R.style.central_btn_setting) {
                    textResource = R.string.btn_charge
                    setOnClickListener { }
                }
            }
            include<LinearLayout>(R.style.basic_layout_setting) {
                include<Button>(R.style.central_btn_setting) {
                    textResource = R.string.btn_userInfo
                    setOnClickListener {

                    }
                }
            }
        }
    }
}
