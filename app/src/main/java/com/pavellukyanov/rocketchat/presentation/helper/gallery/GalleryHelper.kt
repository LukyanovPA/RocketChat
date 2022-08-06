package com.pavellukyanov.rocketchat.presentation.helper.gallery

import android.Manifest
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.presentation.base.BaseFragment
import com.pavellukyanov.rocketchat.presentation.helper.PermissionHelper
import com.pavellukyanov.rocketchat.presentation.helper.ext.findFragment
import javax.inject.Inject

class GalleryHelper @Inject constructor(
    private val permissionHelper: PermissionHelper,
    private val fragmentManager: FragmentManager
) {

    fun pickImagesWithCheckPermission(
        tag: String,
        pickFlag: Int,
        weightLimit: Int? = null,
        resultHandler: (List<FileLocal>?) -> Unit
    ) {
        val fragment = fragmentManager.findFragment(tag) ?: return
        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        permissionHelper.checkPermission(fragment, permissions) {
            pickImagesFromGallery(
                tag,
                pickFlag,
                weightLimit,
                resultHandler
            )
        }
    }

    private fun pickImagesFromGallery(
        tag: String,
        pickFlag: Int,
        weightLimit: Int?,
        resultHandler: (List<FileLocal>) -> Unit
    ) {
        fragmentManager.findFragment(tag)?.let { fragment ->

            (fragment as? BaseFragment<*>)?.imageLauncher?.launch(pickFlag) {
                val localList: ArrayList<FileLocal> = ArrayList(it)
                if (weightLimit != null) {
                    it.forEach {
                        val cursor =
                            fragment.context?.contentResolver?.query(it.uri, null, null, null, null)
                        val sizeIndex = cursor?.getColumnIndex(OpenableColumns.SIZE)
                        cursor?.moveToFirst()
                        sizeIndex?.let { index ->
                            it.size = cursor.getInt(index).div(PHOTO_SIZE_DIV_KB)
                        }
                    }
                    if (localList.find { it.size >= weightLimit } != null) {
                        showPhotoWeightSizeExceededMessage(fragment)
                    }
                    localList.removeAll { it.size >= weightLimit }
                }

                resultHandler(localList)
            }
        }
    }

    private fun showPhotoWeightSizeExceededMessage(fragment: Fragment) {
        Toast.makeText(
            fragment.requireContext(),
            PHOTO_WEIGHT_LIMIT_EXCEEDED,
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        const val PHOTO_WEIGHT_LIMIT_EXCEEDED = R.string.any_screen_show_images_weight_limit
        const val PICK_IMAGE_MULTIPLE = 1
        const val PICK_IMAGE_SINGLE = 2
        const val PHOTO_SIZE_DIV_KB = 5000
    }
}