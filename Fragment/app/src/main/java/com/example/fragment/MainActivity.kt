package com.example.fragment

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var first = FirstFragment()
    var second = SecondFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button6.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                //                add(R.id.container, first)
                replace(R.id.container, first)
                addToBackStack(null)
            }.commit()

        }

        button7.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                //                add(R.id.container,second)
                replace(R.id.container, second)
                addToBackStack(null)
            }.commit()
        }
    }
}
