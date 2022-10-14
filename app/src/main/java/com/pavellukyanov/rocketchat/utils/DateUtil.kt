package com.pavellukyanov.rocketchat.utils

import com.pavellukyanov.rocketchat.utils.Constants.INT_ONE
import com.pavellukyanov.rocketchat.utils.Constants.INT_ZERO
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateUtil {
    private val formatter = DateTimeFormatter.ofPattern("d.MM.yyyy")

    fun localDateToString(date: LocalDate): String =
        date.format(formatter)

    fun longToLocalDate(dateLong: Long): LocalDate =
        Instant.ofEpochMilli(dateLong).atZone(ZoneId.systemDefault()).toLocalDate()

    fun longToDateString(dateLong: Long): String =
        localDateToString(longToLocalDate(dateLong))

    fun dateCompareWithToday(localDate: LocalDate): String {
        val today = LocalDate.now()

        return if (localDate.year != today.year || localDate.monthValue != today.monthValue) {
            localDateToString(localDate)
        } else {
            when (today.dayOfMonth - localDate.dayOfMonth) {
                INT_ONE -> YESTERDAY
                INT_ZERO -> TODAY
                else -> localDateToString(localDate)
            }
        }
    }

    private const val YESTERDAY = "Yesterday"
    private const val TODAY = "Today"
}