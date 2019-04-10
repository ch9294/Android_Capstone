package com.capstone.hanzo.bluebsystem

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class InformationDatabaseHelper private constructor(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "MySystem", null, 1) {

    init {
        instance = this
    }

    companion object {
        private var instance: InformationDatabaseHelper? = null

        @Synchronized
        fun getInstance(ctx: Context) = instance ?: InformationDatabaseHelper(ctx.applicationContext)
    }

    override fun onCreate(db: SQLiteDatabase?) {

        /**
         *  primary key 복합적으로 설정 할 때 사용
         *  아직 Anko 라이브러리에 정식으로 추가되지 않음(2019.03.29)
         *  현재 Anko 라이브러리에 있는 복합 외래키 설정의 형태를 그대로 사용함.
         */
        fun PRIMARY_KEY(vararg columnNames: String): Pair<String, SqlType> {
            return "" to SqlType.create("PRIMARY KEY (${columnNames.joinToString(", ")})")
        }

        db?.run {
            // 버스 정보를 가지는 테이블
            createTable(
                BusNoList.TABLE_NAME, true,
                BusNoList.COLUMN_ID to TEXT + PRIMARY_KEY, // 버스 고유 아이디
                BusNoList.COLUMN_NUM to TEXT, // 버스 번호
                BusNoList.COLUMN_UUID to TEXT, // UUID
                BusNoList.COLUMN_MAJOR to TEXT, // MAJOR
                BusNoList.COLUMN_START to TEXT, // 기점
                BusNoList.COLUMN_END to TEXT // 종점
            )

            // 정류장 정보를 가지는 테이블
            createTable(PlatformArvlInfoList.TABLE_NAME, true,
                PlatformArvlInfoList.COLUMN_ID to TEXT + PRIMARY_KEY, // 정류장 아이디
                PlatformArvlInfoList.COLUMN_NAME to TEXT, // 정류장 이름
                PlatformArvlInfoList.COLUMN_NUM to TEXT // 정류장 번호
            )
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.dropTable(BusNoList.TABLE_NAME)
        db?.dropTable(PlatformArvlInfoList.TABLE_NAME)
    }

}

val Context.database: InformationDatabaseHelper
    get() = InformationDatabaseHelper.getInstance(this)