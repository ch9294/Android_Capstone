package com.capstone.hanzo.bluebsystem

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.Response


// 버스 목록 리스트
data class BusNoList(val busId: String, val busNo: String, val uuid: String, val major: String, val start: String, val end: String) {
    companion object {
        const val URL = "http://13.125.170.17/busInfoSearch.php"
        const val TABLE_NAME = "BusTBL"
        const val COLUMN_ID = "busId"
        const val COLUMN_NUM = "busNo"
        const val COLUMN_UUID = "uuid"
        const val COLUMN_MAJOR = "major"
        const val COLUMN_START = "start"
        const val COLUMN_END = "END"

        fun parseJSON(response: Response): ArrayList<BusNoList> {
            return jacksonObjectMapper().readValue(response.body()?.byteStream()!!)
        }
    }
}

// 정류장 목록 리스트
data class PlatformArvlInfoList(val _id: String, val _number: String, val _name: String) {
    companion object {
        const val URL = "http://13.125.170.17/platformInfoSearch.php"
        const val TABLE_NAME = "PlatformTBL"
        const val COLUMN_ID = "platId"
        const val COLUMN_NUM = "platNo"
        const val COLUMN_NAME = "platName"

        fun parseJSON(response: Response): ArrayList<PlatformArvlInfoList> {
            return jacksonObjectMapper().readValue(response.body()?.byteStream()!!)
        }
    }
}

// 선택한 정류장의 도착정보 리스트
data class PlatformArvlInfoList2(val number: String, val time: String, val type: String)

// 사용자의 정보 리스트
data class UserInfoList(
    val userCash: String,
    val userBook: String,
    val userTrans: String,
    val userGetOffTime: String?,
    val userIn: String,
    val userLastBusNo: String?
)
