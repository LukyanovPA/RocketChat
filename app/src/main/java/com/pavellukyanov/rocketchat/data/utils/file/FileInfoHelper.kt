package com.pavellukyanov.rocketchat.data.utils.file

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import javax.inject.Inject

class FileInfoHelper @Inject constructor(
    private val context: Context
) {

    @SuppressLint("Range")
    fun getFileName(uri: Uri): String? {
        return context.contentResolver.query(uri, null, null, null, null, null)?.let { cursor ->
            cursor.moveToFirst()
            cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)).apply {
                cursor.close()
            }
        }
    }
}