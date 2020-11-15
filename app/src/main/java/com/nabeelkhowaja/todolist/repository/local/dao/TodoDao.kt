package com.nabeelkhowaja.todolist.repository.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nabeelkhowaja.todolist.model.Todo

@Dao
interface TodoDao {

    @Insert
    suspend fun insert(todo: Todo)

    @Query("DELETE FROM Todo where id=:id")
    suspend fun deleteTodo(id: Int)

    @Query("UPDATE Todo set isCompleted=:isCompleted where id=:id")
    suspend fun toggleCompletedStatus(id: Int, isCompleted: Boolean)

    @Query("SELECT * FROM Todo")
    fun getAllTodo(): LiveData<List<Todo>>
}