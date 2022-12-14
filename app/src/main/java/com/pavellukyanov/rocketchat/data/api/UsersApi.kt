package com.pavellukyanov.rocketchat.data.api

import com.pavellukyanov.rocketchat.data.utils.ApiParams
import com.pavellukyanov.rocketchat.data.utils.BaseResponse
import com.pavellukyanov.rocketchat.data.utils.networkadapter.NetworkResponse
import com.pavellukyanov.rocketchat.domain.entity.users.User
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface UsersApi {
    @GET("users/currentUser")
    suspend fun getCurrentUser(): NetworkResponse<BaseResponse<User>>

    @Multipart
    @POST("users/changeAvatar")
    suspend fun changeAvatar(@Part file: MultipartBody.Part?): NetworkResponse<BaseResponse<User>>

    @GET("users/getAllUsers")
    suspend fun getAllUsers(): NetworkResponse<BaseResponse<List<User>>>

    @GET("users/getUser")
    suspend fun getUser(@Query(ApiParams.USER_ID) userId: String): NetworkResponse<BaseResponse<User>>
}