package com.nabeelkhowaja.todolist.model

import com.nabeelkhowaja.todolist.utils.Utils.fillError
import com.nabeelkhowaja.todolist.utils.Utils.sendSuccess

class EntryResponse(
    val username: String,
    val password: String,
    private val confirmPassword: String? = null
) {

    fun isUsernameValid(): ValidatorResult {
        if (username.length >= 4)
            return sendSuccess()
        return fillError("Username must be of at least 4 characters")
    }

    fun isPasswordLengthValid(): ValidatorResult {
        if (password.length >= 4)
            return sendSuccess()
        return fillError("Password must be of at least 4 characters")
    }

    fun validatePasswords(): ValidatorResult {
        if (password == confirmPassword)
            return sendSuccess()
        return fillError("Password doesn't match")
    }

}