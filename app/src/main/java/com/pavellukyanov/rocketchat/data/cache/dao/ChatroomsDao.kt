package com.pavellukyanov.rocketchat.data.cache.dao

import androidx.room.*
import com.pavellukyanov.rocketchat.data.cache.entity.ChatroomLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatroomsDao {
    @Query("SELECT * FROM chatrooms")
    fun getChatrooms(): Flow<List<ChatroomLocal>>

    @Query("SELECT * FROM chatrooms WHERE chatroomId = :chatRoomId")
    fun getChatRoom(chatRoomId: String): ChatroomLocal

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(newList: List<ChatroomLocal>)

    @Delete
    fun delete(chatRoomLocal: ChatroomLocal)
}