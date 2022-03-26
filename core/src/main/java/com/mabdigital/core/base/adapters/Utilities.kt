package com.mabdigital.core.base.adapters

import java.text.SimpleDateFormat
import java.util.*

fun convertIso8601FormatToDateTime(iso8601: String): String? {
    if (iso8601.length <= 10) return ""
    return try {
        val objectDate = convertIso8601ToDate(iso8601)
        val df = SimpleDateFormat("dd MM yyyy", Locale.getDefault())
        objectDate?.run {
            df.format(this)
        }
    } catch (e: Exception) {
        ""
    }
}
fun convertIso8601FormatToDateTimeForDetails(iso8601: String): String? {
    if (iso8601.length <= 10) return ""
    return try {
        val objectDate = convertIso8601ToDate(iso8601)
        val df = SimpleDateFormat("HH:MM - dd MM yyyy", Locale.getDefault())
        objectDate?.run {
            df.format(this)
        }
    } catch (e: Exception) {
        ""
    }
}
private fun convertIso8601ToDate(iso8601: String): Date? {
    return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(iso8601)
}