package com.example.circuit.business.usecase

import com.dropbox.android.external.store4.StoreResponse
import com.example.circuit.coroutine.ICoroutineDispatcherProvider
import com.example.circuit.data.repository.ICountryRepository
import com.example.circuit.data.response.CountryResponse
import dagger.Binds
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface IGetCountryListUseCase {
    suspend operator fun invoke(): Flow<StoreResponse<List<CountryResponse>>>
}

class GetCountryListUseCase @Inject constructor(
    private val dispatcherProvider: ICoroutineDispatcherProvider,
    private val repository: ICountryRepository,
): IGetCountryListUseCase {
    override suspend fun invoke(): Flow<StoreResponse<List<CountryResponse>>> =
        withContext(dispatcherProvider.io) {
            repository.getCountryList()
        }
}