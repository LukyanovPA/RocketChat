package com.pavellukyanov.rocketchat.data.api

import com.pavellukyanov.rocketchat.data.utils.networkadapter.NetworkResponse
import com.pavellukyanov.rocketchat.domain.entity.users.User
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UsersApi {
    @GET("users/currentUser")
    suspend fun getCurrentUser(): NetworkResponse<User>

    @Multipart
    @POST("users/changeAvatar")
    suspend fun changeAvatar(@Part file: MultipartBody.Part?): NetworkResponse<Boolean>
}