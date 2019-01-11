package ch9294.kr.ac.kmu.activityex1

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    val DATA_ACTIVITY = 300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var age: String? = null
        var sex: String? = null

        ageGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.ageTen -> age = "10대"
                R.id.ageTwenty -> age = "20대"
                R.id.ageThirdty -> age = "30대"
                R.id.ageFourty -> age = "40대"
                R.id.ageFifty -> age = "50대 이상"
            }
        }

        sexGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.sex_male -> sex = "남성"
                R.id.sex_female -> sex = "여성"
            }
        }

        registerBtn.setOnClickListener { v ->
            var info = MemberInfo(
                editId.text.toString(),
                editName.text.toString(),
                age,
                sex,
                editTel.text.toString()
            )

            val intent = Intent(this, DataActivity::class.java)
            intent.putExtra("info", info)
            startActivity(intent)
        }

        resetBtn.setOnClickListener { v ->
            editId.text.clear()
            editPw.text.clear()
            editName.text.clear()
            editTel.text.clear()
            ageGroup.clearCheck()
            sexGroup.clearCheck()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            DATA_ACTIVITY -> {
                when (resultCode) {
                    Activity.RESULT_OK -> Toast.makeText(this, "완료!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
