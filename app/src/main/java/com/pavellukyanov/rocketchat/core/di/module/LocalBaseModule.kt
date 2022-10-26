package com.pavellukyanov.rocketchat.core.di.module

import android.content.Context
import androidx.room.Room
import com.pavellukyanov.rocketchat.data.cache.LocalDatabase
import com.pavellukyanov.rocketchat.data.cache.dao.ChatroomsDao
import com.pavellukyanov.rocketchat.data.cache.dao.MessagesDao
import com.pavellukyanov.rocketchat.data.cache.dao.MyAccountDao
import com.pavellukyanov.rocketchat.data.cache.dao.UsersDao
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
        database.chatrooms()

    @Provides
    @Singleton
    fun provideMyAccountDao(database: LocalDatabase): MyAccountDao =
        database.myAccount()

    @Provides
    @Singleton
    fun provideMessagesDao(database: LocalDatabase): MessagesDao =
        database.messages()

    @Provides
    @Singleton
    fun provideUsersDao(database: LocalDatabase): UsersDao =
        database.users()
}