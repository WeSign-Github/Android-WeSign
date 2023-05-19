package com.wesign.wesign.domain

sealed class Response<out R> {
    data class Success<R>(val result: R?): Response<R>()
    data class Error(val exception: Exception?): Response<Nothing>()
    object Loading: Response<Nothing>()
}