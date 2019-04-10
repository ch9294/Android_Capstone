package com.capstone.hanzo.kointest

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.IOException


val module : Module =  org.koin.dsl.module {
    factory {
        object : Callback{
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
    }
}