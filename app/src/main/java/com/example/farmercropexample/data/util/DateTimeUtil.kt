package com.example.farmercropexample.data.util

import android.os.Build
import android.text.format.DateFormat.format
import com.google.gson.internal.bind.util.ISO8601Utils.format
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

object DateTimeUtil {

    fun getDateValue(serverDate: String): String {
        return try {
            val format: DateFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH).also {
                    it.timeZone = TimeZone.getTimeZone("UTC")
                }
            val date = format.parse(serverDate)
            if (date != null)
                SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(date)
            else ""
        } catch (e: Exception) {
            ""
        }
    }
}