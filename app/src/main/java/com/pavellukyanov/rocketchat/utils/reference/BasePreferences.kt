package com.pavellukyanov.rocketchat.utils.reference

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

abstract class BasePreferences(val context: Context) {

    private val preferences: SharedPreferences by lazy {
        val fileName = "rocket_chat_encrypted_references"
        EncryptedSharedPreferences.create(
            context,
            fileName,
            MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun edit() = preferences.edit()

    protected fun saveSync(key: String, value: String?) {
        edit().putString(key, value).commit()
    }

    protected fun getString(key: String, defaultValue: String?): String? {
        return preferences.getString(key, defaultValue)
    }
}