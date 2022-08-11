package com.pavellukyanov.rocketchat.presentation.helper.gallery

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.properties.Delegates

interface File {
    fun getPath(): Any
    fun getType(): MimeType
}

data class FileLocal(
    val uri: Uri,
    val mimeType: MimeType,
    var id: Int? = null
) : File {
    override fun getPath(): Uri = uri
    override fun getType(): MimeType = mimeType

    var size: Int by Delegates.notNull()

    companion object {
        fun map(urlList: List<String>, mimeType: MimeType): List<FileLocal> = urlList.map {
            FileLocal(
                Uri.parse(it),
                mimeType
            )
        }

        fun map(stringUri: String, mimeType: MimeType): FileLocal =
            FileLocal(Uri.parse(stringUri), mimeType)

        fun map(mimeType: MimeType, uriList: List<Uri>): List<FileLocal> = uriList.map {
            FileLocal(
                it,
                mimeType
            )
        }
    }
}

fun FileLocal.getFileName(context: Context): String {
    context.contentResolver.query(
        uri,
        null,
        null,
        null,
        null
    )
        ?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            return cursor.getString(nameIndex)
        }
    throw IllegalStateException("Something goes wrong")
}

@Serializable
enum class MimeType {
    @SerialName("image/jpeg") IMAGE_JPEG,
    @SerialName("image/png") IMAGE_PNG,
    @SerialName("none") NONE
}
