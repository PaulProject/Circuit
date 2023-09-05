package com.example.circuit.extension

import com.dropbox.android.external.store4.StoreResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

sealed interface RequestAction<out T> {

    data object Progress : RequestAction<Nothing>
    data object Empty : RequestAction<Nothing>
    data class Data<T>(val data: T) : RequestAction<T>
    data class Error(val error: Throwable) : RequestAction<Nothing>
}

suspend fun <T> subscribe(request: suspend () -> Flow<StoreResponse<T>>): Flow<RequestAction<T>> =
    request()
        .map { response ->
            when (response) {
                is StoreResponse.Loading -> RequestAction.Progress
                is StoreResponse.Data -> RequestAction.Data(response.requireData())
                is StoreResponse.NoNewData -> RequestAction.Empty
                is StoreResponse.Error.Exception -> RequestAction.Error(response.error)
                is StoreResponse.Error.Message -> RequestAction.Progress
            }
        }
        .catch { emit(RequestAction.Error(it)) }