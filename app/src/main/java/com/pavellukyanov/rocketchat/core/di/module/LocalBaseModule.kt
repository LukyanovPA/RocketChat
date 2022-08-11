package com.pavellukyanov.rocketchat.core.di.module

import android.content.Context
import androidx.room.Room
import com.pavellukyanov.rocketchat.data.cache.LocalDatabase
import com.pavellukyanov.rocketchat.data.cache.dao.ChatroomsDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalBaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): LocalDatabase =
        Room.databaseBuilder(
            context,
            LocalDatabase::class.java,
            "RocketChatDatabase.db"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideChatroomsDao(database: LocalDatabase): ChatroomsDao =
        database.chatroomsDao()
}