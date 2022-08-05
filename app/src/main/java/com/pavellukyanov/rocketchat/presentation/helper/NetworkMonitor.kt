package com.pavellukyanov.rocketchat.presentation.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.pavellukyanov.rocketchat.utils.NoInternetConnection
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NetworkMonitor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}

fun NetworkMonitor.handleInternetConnection(): Flow<Unit> = flow {
    if (!isNetworkAvailable()) throw NoInternetConnection("No internet connection!") else emit(Unit)
}

