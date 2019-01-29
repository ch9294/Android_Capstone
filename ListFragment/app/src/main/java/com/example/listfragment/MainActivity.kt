package com.example.listfragment

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    val listFragment = TestListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tran = supportFragmentManager.beginTransaction()
        tran.apply {
            replace(R.id.container,listFragment)
            commit()
        }
    }
}
