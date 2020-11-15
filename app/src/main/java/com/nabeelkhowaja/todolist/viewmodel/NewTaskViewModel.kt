package com.nabeelkhowaja.todolist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.nabeelkhowaja.todolist.model.Todo
import com.nabeelkhowaja.todolist.repository.TodoRepository

class NewTaskViewModel(application: Application) : AndroidViewModel(application) {

    private var todoRepository: TodoRepository = TodoRepository(application)

    suspend fun insertTask(task: String) = todoRepository.insert(Todo(task, false))

}