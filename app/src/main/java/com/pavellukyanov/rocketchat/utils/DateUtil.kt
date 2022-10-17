package com.pavellukyanov.rocketchat.utils

import com.pavellukyanov.rocketchat.utils.Constants.INT_ONE
import com.pavellukyanov.rocketchat.utils.Constants.INT_ZERO
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object DateUtil {
    private val dateFormatter = DateTimeFormatter.ofPattern("d.MM.yyyy")
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    private fun longToInstant(dateLong: Long): ZonedDateTime =
        Instant.ofEpochMilli(dateLong).atZone(ZoneId.systemDefault())

    fun localDateToStringDate(date: LocalDate): String =
        date.format(dateFormatter)

    fun localDateToStringTime(date: Long): String =
        longToInstant(date).toLocalTime().format(timeFormatter)

    fun longToLocalDate(dateLong: Long): LocalDate =
        longToInstant(dateLong).toLocalDate()

    fun longToDateStringDate(dateLong: Long): String =
        localDateToStringDate(longToLocalDate(dateLong))

    fun dateCompareWithToday(localDate: LocalDate): String {
        val today = LocalDate.now()

        return if (localDate.year != today.year || localDate.monthValue != today.monthValue) {
            localDateToStringDate(localDate)
        } else {
            when (today.dayOfMonth - localDate.dayOfMonth) {
                INT_ONE -> YESTERDAY
                INT_ZERO -> TODAY
                else -> localDateToStringDate(localDate)
            }
        }
    }

    fun longCompareWithToday(longDate: Long): String {
        val localDate = longToLocalDate(longDate)
        val today = LocalDate.now()

        return if (localDate.year != today.year || localDate.monthValue != today.monthValue) {
            localDateToStringDate(localDate)
        } else {
            when (today.dayOfMonth - localDate.dayOfMonth) {
                INT_ONE -> YESTERDAY
                INT_ZERO -> localDateToStringTime(longDate)
                else -> localDateToStringDate(localDate)
            }
        }
    }

    private const val YESTERDAY = "Yesterday"
    private const val TODAY = "Today"
}