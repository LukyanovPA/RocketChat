package com.pavellukyanov.rocketchat.presentation.helper.gallery

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class PickImageContract : ActivityResultContract<Int, List<FileLocal>>() {

    override fun createIntent(context: Context, input: Int): Intent =
        Intent(Intent.ACTION_GET_CONTENT).apply {
            if (input == PICK_IMAGE_MULTIPLE)
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            type = TYPE
        }


    override fun parseResult(resultCode: Int, intent: Intent?): List<FileLocal> {
        val imagesUriList = mutableListOf<FileLocal>()
        if (intent?.clipData != null) {
            val clipData = intent.clipData
            val itemCount = clipData?.itemCount!!
            if (itemCount > 0) {
                for (i in 0 until itemCount) {
                    val item = clipData.getItemAt(i)
                    imagesUriList.add(FileLocal(item.uri, MimeType.IMAGE_JPEG))
                }
            }
        } else if (intent?.data != null) {
            imagesUriList.add(FileLocal(intent.data!!, MimeType.IMAGE_JPEG))
        }
        return imagesUriList
    }

    companion object {
        const val PICK_IMAGE_MULTIPLE = 1
        const val TYPE = "image/*"
    }
}