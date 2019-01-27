package com.example.layoutinflater

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.sub1.*
import org.jetbrains.anko.*

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        button2.setOnClickListener {

            layoutInflater.inflate(R.layout.sub1, find<LinearLayout>(R.id.container))
            textView2.text = intent.getStringExtra("text")
            find<TextView>(R.id.textView2).text = intent.getStringExtra("text")
        }
    }
}