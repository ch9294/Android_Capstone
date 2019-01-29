package com.example.listfragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.ListFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class TestListFragment : ListFragment() {

    lateinit var textView: TextView
    val list = arrayOf("항목1", "항목2", "항목3", "항목4", "항목5")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_test_list, container, false)
        textView = view.findViewById(R.id.textView)

        val adapter = ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, list)

        // 리스트 프래그먼트에서 제공하는 프로퍼티
        // 이 프로퍼티를 이용해서 어댑터를 연결하기 위해서는 프래그먼트의 xml에서 만든 리스트뷰의 아이디를 반드시 @android:id/list로 설정해야함
        listAdapter = adapter

        return view
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)

        val str = list[position]
        textView.text = str
    }
}
