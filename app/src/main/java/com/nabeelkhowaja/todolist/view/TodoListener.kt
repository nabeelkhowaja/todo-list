package com.nabeelkhowaja.todolist.view

interface TodoListener {
    fun deleteTodo(id: Int)
    fun toggleCompletedStatus()
}