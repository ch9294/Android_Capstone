package com.example.capstoneproject.ac.kmu.capstone.dbhelper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

val Context.database: BusNoListHelper
    get() = BusNoListHelper.getInstance(
        applicationContext
    )

class BusNoListHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "BusNoList", null, 1) {

    companion object {
        private var instance: BusNoListHelper? = null

        @Synchronized
        fun getInstance(context: Context) = instance
            ?: BusNoListHelper(context.applicationContext)
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.createTable(
            "BusNoTBL", true,
            "bus_id" to TEXT + PRIMARY_KEY,
            "bus_num" to TEXT + NOT_NULL,
            "uuid" to TEXT,
            "major" to TEXT,
            "start_plat" to TEXT + NOT_NULL,
            "end_plat" to TEXT + NOT_NULL
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
       db?.dropTable("BusNoTBL",true)
    }
}