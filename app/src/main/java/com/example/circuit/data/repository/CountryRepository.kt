package com.example.circuit.data.repository

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import com.dropbox.android.external.store4.fresh
import com.dropbox.android.external.store4.get
import com.example.circuit.coroutine.ICoroutineDispatcherProvider
import com.example.circuit.data.response.CountryResponse
import com.example.circuit.data.service.CountryService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface ICountryRepository {
    suspend fun getCountryList(): Flow<List<CountryResponse>?>
    suspend fun updateCountryList()
}

internal class CountryRepository @Inject constructor(
    private val service: CountryService,
    private val dispatcherProvider: ICoroutineDispatcherProvider,
): ICountryRepository {

    private val countryListStore by lazy {
        StoreBuilder
            .from<Unit, List<CountryResponse>>(fetcher = Fetcher.of { fetchCountryList() })
            .scope(dispatcherProvider.sessionScope)
            .build()
    }

    private suspend fun fetchCountryList(): List<CountryResponse> =
        service.getCountryList()

    override suspend fun getCountryList(): Flow<List<CountryResponse>> {
        val request = StoreRequest.cached(Unit, refresh = true)
        return countryListStore.stream(request)
            .filterDataOrError()
            .map { storeResponse -> storeResponse.requireData() }
    }

    override suspend fun updateCountryList() {
        countryListStore.fresh(Unit)
    }

    private fun <Output : Any> Flow<StoreResponse<Output>>.filterDataOrError(): Flow<StoreResponse<Output>> =
        filterNot { it is StoreResponse.Loading || it is StoreResponse.NoNewData }

}