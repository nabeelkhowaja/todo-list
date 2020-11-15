package com.nabeelkhowaja.todolist.repository.local.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nabeelkhowaja.todolist.model.Todo

@Dao
interface TodoDao {

    @Insert
    suspend fun insert(todo: Todo)

    @Query("DELETE FROM Todo where id=:id")
    suspend fun deleteTask(id: Int)

    @Query("SELECT * FROM Todo")
    fun getAllTodo(): LiveData<List<Todo>>
}