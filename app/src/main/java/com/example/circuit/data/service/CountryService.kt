package com.example.circuit.data.service

import com.example.circuit.data.response.CountryResponse
import retrofit2.http.GET
import java.util.concurrent.Flow

interface CountryService {

    @GET("country-flag-emoji-json@2.0.0/dist/index.json")
    suspend fun getCountryList(): List<CountryResponse>

}