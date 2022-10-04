package com.pavellukyanov.rocketchat.data.utils.file

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class RequestHelper @Inject constructor(
    private val context: Context
) {

    fun generateRequestBody(uri: Uri): RequestBody? {
        return context.contentResolver.openInputStream(uri)?.readBytes()?.toRequestBody(
            contentType = context.contentResolver.getType(uri)?.toMediaTypeOrNull()
        )
    }
}