package com.nabeelkhowaja.todolist.repository

import android.app.Application
import com.nabeelkhowaja.todolist.model.User
import com.nabeelkhowaja.todolist.repository.local.AppDatabase
import kotlinx.coroutines.flow.first

class UserRepository(application: Application) {

    var database = AppDatabase.getInstance(application)
    var userDao = database.userDao()

    suspend fun insert(user: User) = userDao.insert(user)

    suspend fun getUser(username: String): User? {
        return try {
            userDao.getUserByUsername(username).first()
        }catch (exception: NoSuchElementException){
            null
        }
    }
}