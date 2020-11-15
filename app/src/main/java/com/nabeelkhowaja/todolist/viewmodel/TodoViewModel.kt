package com.nabeelkhowaja.todolist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.nabeelkhowaja.todolist.model.Todo
import com.nabeelkhowaja.todolist.repository.TodoRepository

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private var todoRepository: TodoRepository = TodoRepository(application)

    suspend fun insertTask(task: String) = todoRepository.insert(Todo(task, false))

    fun getAllTodo(): LiveData<List<Todo>> = todoRepository.getAllTodo()

    suspend fun deleteTask(id: Int) = todoRepository.deleteTask(id)

    suspend fun toggleCompletedStatus(id: Int, isCompleted: Boolean) = todoRepository.toggleCompletedStatus(id, isCompleted)

}