package com.pavellukyanov.rocketchat.data.cache.dao

import androidx.room.*
import com.pavellukyanov.rocketchat.data.cache.entity.ChatroomLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatroomsDao {
    @Query("SELECT * FROM chatrooms")
    fun getChatroomsStream(): Flow<List<ChatroomLocal>>

    @Query("SELECT * FROM chatrooms")
    fun getChatrooms(): List<ChatroomLocal>

    @Query("SELECT * FROM chatrooms WHERE chatroomId = :chatRoomId")
    suspend fun getChatRoom(chatRoomId: String): ChatroomLocal

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(newList: List<ChatroomLocal>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(chatroomLocal: ChatroomLocal)

    @Delete
    suspend fun delete(chatRoomLocal: ChatroomLocal)

    @Delete
    suspend fun delete(chatRoomLocalList: List<ChatroomLocal>)

    @Query("DELETE FROM chatrooms")
    suspend fun delete()
}