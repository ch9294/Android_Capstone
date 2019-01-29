package com.example.activitycontroller


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import java.util.jar.Pack200


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ResultFragment : Fragment() {

    lateinit var button2:Button
    lateinit var textView1:TextView
    lateinit var textView2:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_result, container, false)
        val mainActivity = activity as MainActivity

        button2 = view.findViewById(R.id.button2)
        textView1 = view.findViewById(R.id.textView)
        textView2 = view.findViewById(R.id.textView2)

        textView1.text = mainActivity.value1
        textView2.text = mainActivity.value2

        button2.setOnClickListener {
            // 백 스택에서 자기 자신을 제거하고 이전으로 돌아간다
            mainActivity.supportFragmentManager.popBackStack()
        }
        return view
    }


}
