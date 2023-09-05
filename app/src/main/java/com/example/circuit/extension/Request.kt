package com.example.circuit.extension

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

sealed interface RequestAction<out T> {

    data object Progress : RequestAction<Nothing>
    data class Data<T>(val data: T) : RequestAction<T>
    data class Error(val error: Throwable) : RequestAction<Nothing>
}

suspend fun <T> requestFlow(request: suspend () -> Flow<T>): Flow<RequestAction<T>> =
    request()
        .map<T, RequestAction<T>> { RequestAction.Data(it) }
        .catch { emit(RequestAction.Error(it)) }