package com.pavellukyanov.rocketchat.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pavellukyanov.rocketchat.domain.entity.home.MyAccount
import kotlinx.coroutines.flow.Flow

@Dao
interface MyAccountDao {
    @Query("SELECT * FROM my_account")
    fun getMyAccount(): Flow<List<MyAccount>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(myAccount: MyAccount)

    @Query("DELETE FROM my_account")
    suspend fun delete()
}