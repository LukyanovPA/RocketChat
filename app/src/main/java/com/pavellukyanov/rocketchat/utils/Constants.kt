package com.pavellukyanov.rocketchat.utils

import com.pavellukyanov.rocketchat.BuildConfig

object Constants {
    //Int
    const val INT_ZERO = 0
    const val INT_ONE = 1
    const val INT_TWO = 2
    const val INT_THREE = 3
    const val INT_TWENTY = 20

    //Long
    const val LONG_FIFTY = 50L
    const val SPLASH_SCREEN_DELAY = 2000L
    const val TIMEOUT = 30L

    //String
    const val EMPTY_STRING = ""

    //Common
    const val AVATAR_PLACEHOLDER =
        "android.resource://com.pavellukyanov.rocketchat/drawable/ic_avatar_placeholder"
    const val WS_URL = "ws://${BuildConfig.BASE_URL}/"
}