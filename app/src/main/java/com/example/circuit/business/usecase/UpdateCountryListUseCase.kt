package com.example.circuit.business.usecase

import com.example.circuit.coroutine.ICoroutineDispatcherProvider
import com.example.circuit.data.repository.ICountryRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface IUpdateCountryListUseCase {
    suspend operator fun invoke()
}

class UpdateCountryListUseCase @Inject constructor(
    private val dispatcherProvider: ICoroutineDispatcherProvider,
    private val repository: ICountryRepository,
): IUpdateCountryListUseCase {
    override suspend fun invoke() {
        withContext(dispatcherProvider.io) {
            repository.updateCountryList()
        }
    }
}