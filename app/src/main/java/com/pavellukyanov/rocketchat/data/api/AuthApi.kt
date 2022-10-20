package com.pavellukyanov.rocketchat.data.api

import com.pavellukyanov.rocketchat.data.utils.ApiParams
import com.pavellukyanov.rocketchat.data.utils.BaseResponse
import com.pavellukyanov.rocketchat.data.utils.networkadapter.NetworkResponse
import com.pavellukyanov.rocketchat.domain.entity.auth.SignInRequest
import com.pavellukyanov.rocketchat.domain.entity.auth.SignUpRequest
import com.pavellukyanov.rocketchat.domain.entity.auth.TokenResponse
import com.pavellukyanov.rocketchat.domain.entity.users.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @POST("auth/signup")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): NetworkResponse<BaseResponse<TokenResponse>>

    @POST("auth/signin")
    suspend fun signIn(@Body signInRequest: SignInRequest): NetworkResponse<BaseResponse<TokenResponse>>

    @POST("auth/updateToken")
    fun updateToken(@Query(ApiParams.REFRESH_TOKEN) refreshToken: String?): Call<TokenResponse>

    @GET("auth/logout")
    suspend fun logout(): NetworkResponse<BaseResponse<Boolean>>

    companion object {
        const val REFRESH_URL = "auth/updateToken"
    }
}