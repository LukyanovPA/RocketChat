package com.pavellukyanov.rocketchat.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pavellukyanov.rocketchat.data.cache.dao.ChatroomsDao
import com.pavellukyanov.rocketchat.data.cache.dao.MyAccountDao
import com.pavellukyanov.rocketchat.data.cache.entity.ChatroomLocal
import com.pavellukyanov.rocketchat.domain.entity.home.MyAccount

@Database(
    entities = [
        ChatroomLocal::class,
        MyAccount::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(MyAccountConverter::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun chatroomsDao(): ChatroomsDao
    abstract fun myAccountDao(): MyAccountDao
}