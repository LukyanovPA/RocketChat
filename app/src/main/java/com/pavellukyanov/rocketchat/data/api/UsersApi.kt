package com.pavellukyanov.rocketchat.data.api

import com.pavellukyanov.rocketchat.data.utils.ApiParams
import com.pavellukyanov.rocketchat.data.utils.networkadapter.NetworkResponse
import com.pavellukyanov.rocketchat.domain.entity.users.User
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UsersApi {
    @GET("users/currentUser")
    suspend fun getCurrentUser(): NetworkResponse<User>

    @POST("users/changeAvatar")
    suspend fun changeAvatar(@Query(ApiParams.AVATAR) avatar: String?): NetworkResponse<Boolean>
}