package com.pavellukyanov.rocketchat.presentation.helper

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.presentation.MainActivity
import com.pavellukyanov.rocketchat.presentation.base.BaseFragment
import javax.inject.Inject

class PermissionHelper @Inject constructor() {

    fun checkPermission(
        fragment: Fragment,
        permissions: Array<String>,
        onPermissionsGranted: () -> Unit
    ) {
        if (permissions.all {
                ContextCompat.checkSelfPermission(
                    fragment.requireContext(),
                    it
                ) == PackageManager.PERMISSION_GRANTED
            }) {
            onPermissionsGranted()
        } else {
            requestPermission(fragment, permissions, onPermissionsGranted)
        }
    }

    fun checkPermission(
        activity: AppCompatActivity,
        permissions: Array<String>,
        onPermissionsGranted: () -> Unit
    ) {
        if (permissions.all {
                ContextCompat.checkSelfPermission(
                    activity,
                    it
                ) == PackageManager.PERMISSION_GRANTED
            }) {
            onPermissionsGranted()
        } else {
            requestPermission(activity, permissions, onPermissionsGranted)
        }

    }

    private fun requestPermission(
        fragment: Fragment,
        permissions: Array<String>,
        permissionsResultHandler: () -> Unit
    ) {
        (fragment as? BaseFragment<*>)?.permissionLauncher?.launch(permissions) { grantResult ->
            if (grantResult.isNotEmpty() && grantResult.all { it.value }) permissionsResultHandler()
            else showAccessDeniedMessage(fragment.requireContext())
        }
    }

    private fun requestPermission(
        activity: AppCompatActivity,
        permissions: Array<String>,
        permissionsResultHandler: () -> Unit
    ) {
        (activity as? MainActivity)?.permissionLauncher?.launch(permissions) { grantResult ->
            if (grantResult.isNotEmpty() && grantResult.all { it.value }) permissionsResultHandler()
            else showAccessDeniedMessage(activity)
        }
    }

    private fun showAccessDeniedMessage(context: Context) {
        Toast.makeText(
            context,
            ACCESS_DENIED_MSG,
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        const val ACCESS_DENIED_MSG = R.string.any_screen_access_denied
    }
}