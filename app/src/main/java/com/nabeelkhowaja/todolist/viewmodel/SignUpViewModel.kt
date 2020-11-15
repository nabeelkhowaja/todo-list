package com.nabeelkhowaja.todolist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.nabeelkhowaja.todolist.utils.SecurityUtils
import com.nabeelkhowaja.todolist.model.SignUpResponse
import com.nabeelkhowaja.todolist.model.User
import com.nabeelkhowaja.todolist.repository.UserRepository
import kotlinx.coroutines.flow.flow

class SignUpViewModel(application: Application) : AndroidViewModel(application) {

    /*private val _signUpResponse = SingleLiveEvent<SignUpResponse>()
    val signUpResponse: SingleLiveEvent<SignUpResponse>
        get() = _signUpResponse*/
    private var userRepository: UserRepository = UserRepository(application)

    suspend fun doSignUp(username: String, password: String, confirmPassword: String) = flow {
        //emitting flow for validation and db insertion
        emit(SignUpResponse(username, password, confirmPassword))
    }

    /*fun doSignUp(username: String, password: String, confirmPassword: String) {
        _signUpResponse.value = SignUpResponse(username, password, confirmPassword)
    }*/

    suspend fun insertUser(username: String, password: String) {
        //Saving encrypted password in db
        val encryptedPassword = SecurityUtils.encrypt(password, username)
        userRepository.insert(User(username, encryptedPassword!!))
    }

    suspend fun getUser(username: String): User? = userRepository.getUser(username)

}