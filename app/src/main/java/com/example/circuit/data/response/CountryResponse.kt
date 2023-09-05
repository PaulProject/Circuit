package com.example.circuit.data.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CountryResponse(
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "image")
    val image: String? = null,
)