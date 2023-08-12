package com.example.trackingcontrol.utils

sealed class Resource<out R> {
    data class Success<out R>(val result: R) : Resource<R>()
    data class Failure(val exception: Exception) : Resource<Nothing>()
    object Loading : Resource<Nothing>()

    fun toData(): R? = if(this is Success) this.result else null
}