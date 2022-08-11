package com.pavellukyanov.rocketchat.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateUtil {
    private val formatter = DateTimeFormatter.ofPattern("d.MM.yyyy")

    fun localDateToString(date: LocalDate): String =
        date.format(formatter)
}