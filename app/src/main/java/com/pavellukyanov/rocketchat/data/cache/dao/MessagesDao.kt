package com.pavellukyanov.rocketchat.data.cache.dao

import androidx.room.*
import com.pavellukyanov.rocketchat.domain.entity.chatroom.chat.ChatMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface MessagesDao {
    @Query("SELECT * FROM messages WHERE chatroomId = :chatroomId")
    fun getMessages(chatroomId: String): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(newList: List<ChatMessage>)

    @Delete
    fun delete(oldList: List<ChatMessage>)
}