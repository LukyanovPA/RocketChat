package com.pavellukyanov.rocketchat.utils

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
}