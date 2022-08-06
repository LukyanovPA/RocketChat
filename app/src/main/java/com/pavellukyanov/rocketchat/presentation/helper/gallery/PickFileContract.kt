package com.pavellukyanov.rocketchat.presentation.helper.gallery

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class PickFileContract : ActivityResultContract<Int, List<FileLocal>>() {

    override fun createIntent(context: Context, input: Int): Intent =
        Intent(Intent.ACTION_GET_CONTENT).apply {
            type = TYPE
        }

    override fun parseResult(resultCode: Int, intent: Intent?): List<FileLocal> {
        val uriList = mutableListOf<FileLocal>()
        if (intent?.clipData != null) {
            val clipData = intent.clipData
            val itemCount = clipData?.itemCount!!
            if (itemCount > 0) {
                for (i in 0 until itemCount) {
                    val item = clipData.getItemAt(i)
                    uriList.add(FileLocal(item.uri, MimeType.NONE))
                }
            }
        } else if (intent?.data != null) {
            uriList.add(FileLocal(intent.data!!, MimeType.NONE))
        }
        return uriList
    }

    companion object {
        const val TYPE = "*/*"
    }
}