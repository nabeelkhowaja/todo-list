package com.nabeelkhowaja.todolist.view

interface TodoListener {
    fun deleteTodo(id: Int)
    fun toggleCompletedStatus(id: Int, isCompleted: Boolean)
}