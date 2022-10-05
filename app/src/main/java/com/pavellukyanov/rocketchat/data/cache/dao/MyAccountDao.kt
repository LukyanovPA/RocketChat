package com.pavellukyanov.rocketchat.data.cache.dao

import androidx.room.*
import com.pavellukyanov.rocketchat.domain.entity.home.MyAccount
import kotlinx.coroutines.flow.Flow

@Dao
interface MyAccountDao {
    @Query("SELECT * FROM my_account")
    fun getMyAccount(): Flow<List<MyAccount>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(myAccount: MyAccount)

    @Delete
    fun delete(myAccount: MyAccount)
}