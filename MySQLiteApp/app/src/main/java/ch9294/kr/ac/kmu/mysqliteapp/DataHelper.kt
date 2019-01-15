package ch9294.kr.ac.kmu.mysqliteapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataHelper(context: Context) : SQLiteOpenHelper(context, "Test.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {

        val sql = """
            create table Person(
            idx integer primary key autoincrement,
            name text not null,
            address text not null
            )
        """.trimIndent()

        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}