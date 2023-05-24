package com.wesign.wesign.domain

sealed class Resource<out R> {
    data class Success<R>(val result: R?): Resource<R>()
    data class Error(val exception: Exception?): Resource<Nothing>()
    object Loading: Resource<Nothing>()
}