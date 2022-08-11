package com.pavellukyanov.rocketchat.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pavellukyanov.rocketchat.data.cache.dao.ChatroomsDao
import com.pavellukyanov.rocketchat.data.cache.entity.ChatroomLocal

@Database(
    entities = [
        ChatroomLocal::class
    ],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun chatroomsDao(): ChatroomsDao
}