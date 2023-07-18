package com.mohamednader.shoponthego.Network

sealed class ApiState<out T> {
    data class Success<T>(val data: T) : ApiState<T>()
    data class Failure(val msg: Throwable) : ApiState<Nothing>()
    object Loading : ApiState<Nothing>()
}