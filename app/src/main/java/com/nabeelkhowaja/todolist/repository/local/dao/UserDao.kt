package com.nabeelkhowaja.todolist.repository.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nabeelkhowaja.todolist.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM User WHERE username =:username")
    fun getUserByUsername(username: String): Flow<User>
}