package com.nabeelkhowaja.todolist.viewmodel

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.nabeelkhowaja.todolist.model.EntryResponse
import com.nabeelkhowaja.todolist.model.User
import com.nabeelkhowaja.todolist.repository.UserRepository
import com.nabeelkhowaja.todolist.utils.Constants.USER
import kotlinx.coroutines.flow.flow

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private var userRepository: UserRepository = UserRepository(application)
    var preferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(application)

    suspend fun doSignIn(username: String, password: String) = flow {
        //emitting flow for validation and db insertion
        emit(EntryResponse(username, password))
    }

    suspend fun getUser(username: String): User? = userRepository.getUser(username)

    //Emitting last saved user
    fun getLastSavedUser(): LiveData<User> {
        val lastUser = MutableLiveData<User>()
        lastUser.value = getLastUser()
        return lastUser
    }

    //Saving user object as json
    fun saveUser(username: String, password: String) {
        val user = User(username, password)
        val json = Gson().toJson(user)
        preferences.edit().putString(USER, json).apply()
    }

    private fun getLastUser(): User? {
        val json = preferences.getString(USER, null)
        if (json != null)
            return Gson().fromJson(json, User::class.java)
        return null
    }

    //Use to remove the user if "Remember me" checkbox is unchecked
    fun removeUser() {
        preferences.edit().putString(USER, null).apply()
    }
}