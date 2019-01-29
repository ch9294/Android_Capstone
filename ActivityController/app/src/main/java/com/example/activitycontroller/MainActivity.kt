package com.example.activitycontroller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    val inputFragment = InputFragment()
    val resultFragment = ResultFragment()

    lateinit var value1:String
    lateinit var value2:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setFragment("input")
    }

    fun setFragment(name: String) {
        val tran = supportFragmentManager.beginTransaction()

        name.let {
            when (it) {
                "input" -> {
                    tran.apply {
                        replace(R.id.container, inputFragment)
                        commit()
                    }
                }

                "result" -> {
                    tran.apply {
                        replace(R.id.container, resultFragment)
                        commit()
                        addToBackStack(null)
                    }
                }

                else -> {

                }
            }
        }
    }
}
