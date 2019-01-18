package ch9294.kr.ac.kmu.beaconsystem

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private val select_list = listOf<EditText>(editID, editPW, editPW2, editName)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        /*
        가입하기 버튼
         - 아이디, 비밀번호, 비밀번호 확인, 이름의 경우 모든 내용이 필수 입력사항이므로 하나라도 입력이 되어 있지 않을 경우
         토스트 창으로 입력을 유도하게 함
         */
        submitBtn.setOnClickListener {
            if (!select_list.checkNotNull()) {
                // 토스트보다는 별도의 다이얼로그창을 구현하도록 한다.
                Toast.makeText(this, "모두 입력해주세요", Toast.LENGTH_SHORT).show()
            }

            /*
            로그인 화면으로 돌아갈 때 자신이 회원가입 시 입력하였던 아이디와 비밀번호의 정보를
            인텐트를 통해서 로그인 화면으로 전달한다. 그렇게해서 로그인 창에 자동으로 아이디와 비밀번호를 입력시켜 놓음
             */
            intent.putExtra("id", editID.text.toString())
            intent.putExtra("pw", editPW.text.toString())

            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        /*
        리셋 버튼
         - 회원가입 액티비티에 존재하는 모든 입력창의 내용을 초기화한다.
         - 라디오 버튼의 경우 기존의 정해진 초기값으로 초기화한다.
         */
        resetBtn.setOnClickListener {
            for (v in select_list) {
                // editText 초기화
                v.text = null
            }
            radioYouth.isChecked = true
            radioMale.isChecked = true
        }

        /*
        돌아가기 버튼
         - 로그인 화면으로 돌아가는 버튼이다. 이 버튼을 누르면 기존에 작성하던 항목은 모두 초기화가 된다.
         */
        returnBtn.setOnClickListener {

        }

        checkBtn.setOnClickListener {

        }
    }

    /*
    돌아가기 버튼과 동일한 동작을 하기 때문에 일단 막아 놓는다
     */
    override fun onBackPressed() {
        Toast.makeText(this, "해당 화면에서는 돌아가기 버튼을 이용해 주세요", Toast.LENGTH_SHORT).show()
    }
}

