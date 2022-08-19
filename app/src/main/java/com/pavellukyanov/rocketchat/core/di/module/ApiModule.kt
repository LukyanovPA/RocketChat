package com.pavellukyanov.rocketchat.core.di.module

import com.google.gson.GsonBuilder
import com.pavellukyanov.rocketchat.data.api.AuthApi
import com.pavellukyanov.rocketchat.data.utils.RetrofitClient
import com.pavellukyanov.rocketchat.data.utils.networkadapter.NetworkResponseAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApiModule {

    @Singleton
    @Provides
    fun provideRetrofitClient(client: RetrofitClient): Retrofit =
        Retrofit.Builder()
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .baseUrl(BASE_URL)
            .client(client.createLoggedHttpClient())
            .build()

    @Singleton
    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    companion object {
        private const val BASE_URL = "https://rocket-chat-api.herokuapp.com/api/"
    }
}