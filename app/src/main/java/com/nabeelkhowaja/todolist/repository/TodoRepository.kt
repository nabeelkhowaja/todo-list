package com.nabeelkhowaja.todolist.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.nabeelkhowaja.todolist.model.Todo
import com.nabeelkhowaja.todolist.repository.local.AppDatabase

class TodoRepository(application: Application) {

    var database = AppDatabase.getInstance(application)
    var todoDao = database.todoDao()

    suspend fun insert(todo: Todo) = todoDao.insert(todo)

    suspend fun deleteTask(id: Int) = todoDao.deleteTask(id)

    fun getAllTodo(): LiveData<List<Todo>> = todoDao.getAllTodo()

    suspend fun toggleCompletedStatus(id: Int, isCompleted: Boolean) = todoDao.toggleCompletedStatus(id, isCompleted)

}