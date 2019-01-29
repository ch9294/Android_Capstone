package com.example.activitycontroller


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.fragment_result.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class InputFragment : Fragment() {

    private lateinit var button: Button
    private lateinit var edit1: EditText
    private lateinit var edit2: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_input, container, false)

        /*
        프래그먼트에서는 액티비티와 같이 xml의 뷰들을 즉시 프로퍼티로 가져오는 것이 불가능하다
        기존 자바에서 쓰던 방식과 같이 xml에서 설정한 아이디를 가지고 코틀린 코드와 연결시켜 주어야
        한다.
         */

        button = view.findViewById(R.id.button)
        edit1 = view.findViewById(R.id.editText)
        edit2 = view.findViewById(R.id.editText2)

        button.setOnClickListener {
            // 프래그먼트는 자기 자신을 관리하고 있는 액티비티를 프로퍼티로 가지고 있음
            val mainActivity = activity as MainActivity
            mainActivity.value1 = edit1.text.toString()
            mainActivity.value2 = edit2.text.toString()
            mainActivity.setFragment("result")

        }
        return view
    }

}
