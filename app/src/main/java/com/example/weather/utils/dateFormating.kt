package com.example.weather.utils

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
internal val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
@RequiresApi(Build.VERSION_CODES.O)
internal val formatter2 = DateTimeFormatter.ofPattern("MMMM dd")
@RequiresApi(Build.VERSION_CODES.O)
internal val formatter3 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

@RequiresApi(Build.VERSION_CODES.O)
fun getDayOfWeek(date: String): String = LocalDate.parse(date).dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)

@RequiresApi(Build.VERSION_CODES.O)
fun getMonth(date: String): String = LocalDate.parse(date, formatter).month.toString()

@RequiresApi(Build.VERSION_CODES.O)
fun getDayOfMonth(date: String): String = LocalDate.parse(date, formatter).dayOfMonth.toString()

@RequiresApi(Build.VERSION_CODES.O)
fun getDayAndMonth(date: String): String = LocalDate.parse(date.take(10)).format(formatter2)

@RequiresApi(Build.VERSION_CODES.O)
fun getHour(date: String): String =  date.takeLast(4)