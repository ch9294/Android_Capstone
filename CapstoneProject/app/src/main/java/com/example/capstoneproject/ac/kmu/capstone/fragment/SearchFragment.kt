package com.example.capstoneproject.ac.kmu.capstone.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.RadioGroup
import android.widget.SimpleAdapter
import com.example.capstoneproject.R
import com.example.capstoneproject.ac.kmu.capstone.activity.BusNoList
import com.example.capstoneproject.ac.kmu.capstone.activity.HomeActivity
import com.example.capstoneproject.ac.kmu.capstone.dbhelper.database
import kotlinx.android.synthetic.main.fragment_search.*
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class SearchFragment : Fragment(),AnkoLogger {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val controller = activity as HomeActivity
        val searchView = view.find<android.support.v7.widget.SearchView>(R.id.searchView)
        val searchGrp = view.find<RadioGroup>(R.id.searchGrp)
        val searchList = view.find<ListView>(R.id.searchList)
        val keys = arrayOf("busNum", "start", "end")
        val ids = intArrayOf(
            R.id.textView,
            R.id.textView2,
            R.id.textView3
        )

        val list = makeList(controller.database.use {
            select("BusNoTBL").parseList(classParser<BusNoList>())
        })

        searchList.adapter = SimpleAdapter(controller,list,
            R.layout.bus_no_listview,keys,ids)

        searchGrp.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioBus -> {
                    searchView.queryHint = "버스 번호를 입력해주세요"
                }
                R.id.radioPlatform -> {
                    searchView.queryHint = "정류장 이름을 입력해주세요"
                }
            }
        }

        searchList.isTextFilterEnabled = true
        searchView.setOnQueryTextListener(QueryListener(searchList))
        return view
    }

    private fun makeList(list: List<BusNoList>): ArrayList<Map<String, String>> {
        val arrList = ArrayList<Map<String, String>>()

        list.forEach {
            val map = mapOf(
                "busNum" to it.busNo,
                "start" to it.start,
                "end" to it.end
            )
            arrList.add(map)
        }
        return arrList
    }

    inner class QueryListener(val listView:ListView) : android.support.v7.widget.SearchView.OnQueryTextListener {
        override fun onQueryTextChange(newText: String?): Boolean {
            listView.setFilterText(newText)
            if (newText?.length == 0) {
                listView.clearTextFilter()
            }
            return true
        }

        override fun onQueryTextSubmit(p0: String?): Boolean {
            searchView.clearFocus()
            return true
        }
    }
}
