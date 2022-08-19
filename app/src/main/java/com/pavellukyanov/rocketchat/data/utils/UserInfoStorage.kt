package com.pavellukyanov.rocketchat.data.utils

import android.content.Context
import com.google.gson.Gson
import com.pavellukyanov.rocketchat.domain.entity.auth.TokenResponse
import com.pavellukyanov.rocketchat.domain.entity.users.User
import com.pavellukyanov.rocketchat.domain.utils.UserInfo
import com.pavellukyanov.rocketchat.utils.reference.BasePreferences
import javax.inject.Inject

class UserInfoStorage @Inject constructor(
    context: Context,
    private val gson: Gson
) : BasePreferences(context), UserInfo {

    override var tokens: TokenResponse?
        get() = try {
            gson.fromJson(tokenInfo, TokenResponse::class.java)
        } catch (e: Exception) {
            null
        }
        set(value) {
            tokenInfo = gson.toJson(value).toString()
        }

    private var tokenInfo: String?
        get() = getString(TOKEN_KEY, null)
        set(value) {
            saveSync(TOKEN_KEY, value)
        }


    override var user: User? = null

    companion object {
        const val TOKEN_KEY = "TOKEN_KEY"
    }
}