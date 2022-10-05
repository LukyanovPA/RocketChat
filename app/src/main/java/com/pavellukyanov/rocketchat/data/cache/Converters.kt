package com.pavellukyanov.rocketchat.data.cache

import android.net.Uri
import androidx.room.TypeConverter

class MyAccountConverter {
    @TypeConverter
    fun convertToString(uri: Uri): String = uri.toString()

    @TypeConverter
    fun convertToUri(stringUri: String): Uri = Uri.parse(stringUri)
}