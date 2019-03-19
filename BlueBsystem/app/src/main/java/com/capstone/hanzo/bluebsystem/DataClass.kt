package com.capstone.hanzo.bluebsystem

// 버스 목록 리스트
data class BusNoList(
    val busId: String,
    val busNo: String,
    val uuid: String,
    val major: String,
    val start: String,
    val end: String
)

// 정류장 목록 리스트
data class PlatformArvlInfoList(val platId: String, val platNo: String, val platName: String)

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