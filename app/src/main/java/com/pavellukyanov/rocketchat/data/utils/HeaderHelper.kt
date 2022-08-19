package com.pavellukyanov.rocketchat.data.utils

import com.pavellukyanov.rocketchat.BuildConfig
import com.pavellukyanov.rocketchat.domain.utils.UserInfo
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeaderHelper @Inject constructor(
    private val userInfoStorage: UserInfo
) {
    fun getHeaders(): HashMap<String, String?> {
        return hashMapOf<String, String?>().also {
            if (BuildConfig.DEBUG) {
                Timber.d("UserToken ${userInfoStorage.tokens?.token}")
            }
            it[TOKEN] = BEARER + userInfoStorage.tokens?.token
        }
    }

    companion object {
        private const val TOKEN = "Authorization"
        private const val BEARER = "Bearer "
    }
}