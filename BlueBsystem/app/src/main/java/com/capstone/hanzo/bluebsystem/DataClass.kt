package com.capstone.hanzo.bluebsystem

data class BusNoList(
    val busId: String,
    val busNo: String,
    val uuid: String,
    val major: String,
    val start: String,
    val end: String
)


data class PlatformArvlInfoList(val platId: String, val platNo: String, val platName: String)

data class PlatformArvlInfoList2(val number: String, val time: String, val type: String)