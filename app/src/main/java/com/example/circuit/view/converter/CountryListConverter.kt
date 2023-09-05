package com.example.circuit.view.converter

import com.example.circuit.data.response.CountryResponse
import com.example.circuit.view.item.CountryItem
import javax.inject.Inject

interface ICountryListConverter {
    fun convert(list: List<CountryResponse>): List<CountryItem>
}

class CountryListConverter @Inject constructor() : ICountryListConverter {
    override fun convert(list: List<CountryResponse>): List<CountryItem> =
        list.map { country ->
            CountryItem(
                name = country.name.orEmpty(),
                image = country.image.orEmpty()
            )
        }

}