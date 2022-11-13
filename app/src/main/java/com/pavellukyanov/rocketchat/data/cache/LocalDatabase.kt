package com.pavellukyanov.rocketchat.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pavellukyanov.rocketchat.data.cache.dao.ChatroomsDao
import com.pavellukyanov.rocketchat.data.cache.dao.MessagesDao
import com.pavellukyanov.rocketchat.data.cache.dao.UsersDao
import com.pavellukyanov.rocketchat.data.cache.entity.ChatroomLocal
import com.pavellukyanov.rocketchat.domain.entity.chatroom.chat.ChatMessage
import com.pavellukyanov.rocketchat.domain.entity.users.User

@Database(
    entities = [
        ChatroomLocal::class,
        ChatMessage::class,
        User::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(MyAccountConverter::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun chatrooms(): ChatroomsDao
    abstract fun messages(): MessagesDao
    abstract fun users(): UsersDao
}