package com.github.qoiu.wordly.firebase

import java.lang.Exception

sealed class DbResponse {
    data class Success<T>(val list: T): DbResponse()
    data class Error(val e: Exception): DbResponse()
}