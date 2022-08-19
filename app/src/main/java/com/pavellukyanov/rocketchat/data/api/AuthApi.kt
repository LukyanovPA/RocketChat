package com.pavellukyanov.rocketchat.data.api

import com.pavellukyanov.rocketchat.data.utils.ApiParams
import com.pavellukyanov.rocketchat.data.utils.ObjectResponse
import com.pavellukyanov.rocketchat.data.utils.networkadapter.NetworkResponse
import com.pavellukyanov.rocketchat.domain.entity.auth.SignInRequest
import com.pavellukyanov.rocketchat.domain.entity.auth.SignUpRequest
import com.pavellukyanov.rocketchat.domain.entity.auth.TokenResponse
import com.pavellukyanov.rocketchat.domain.entity.users.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @POST("auth/signUp")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): NetworkResponse<ObjectResponse<TokenResponse>>

    @POST("auth/signIn")
    suspend fun signIn(@Body signInRequest: SignInRequest): NetworkResponse<ObjectResponse<TokenResponse>>

    @GET("auth/currentUser")
    suspend fun getCurrentUser(): NetworkResponse<ObjectResponse<User>>

    @POST("auth/updateToken")
    fun updateToken(@Query(ApiParams.REFRESH_TOKEN) refreshToken: String?): NetworkResponse<ObjectResponse<TokenResponse>>

    companion object {
        const val REFRESH_URL = "auth/updateToken"
    }
}