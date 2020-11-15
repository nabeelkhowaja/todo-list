package com.nabeelkhowaja.todolist.utils

import com.nabeelkhowaja.todolist.model.ValidatorResult

object Utils {

    fun sendSuccess(): ValidatorResult {
        val result = ValidatorResult()
        result.message = null
        result.isValid = true
        return result
    }

    fun fillError(error: String): ValidatorResult {
        val result = ValidatorResult()
        result.message = error
        result.isValid = false
        return result
    }
}