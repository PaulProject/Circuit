package com.example.circuit.view.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.circuit.business.usecase.IGetCountryListUseCase
import com.example.circuit.business.usecase.IUpdateCountryListUseCase
import com.example.circuit.data.response.CountryResponse
import com.example.circuit.extension.RequestAction
import com.example.circuit.extension.subscribe
import com.example.circuit.view.converter.ICountryListConverter
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import javax.inject.Inject

class CountryListPresenterFactory @Inject constructor(
    private val getCountryList: IGetCountryListUseCase,
    private val countryListConverter: ICountryListConverter,
    private val updateCountryList: IUpdateCountryListUseCase
) : Presenter.Factory {

    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext
    ): Presenter<*>? {
        return when (screen) {
            is CountryListScreen -> CountryListPresenter(
                getCountryList = getCountryList,
                countryListConverter = countryListConverter,
                updateCountryList = updateCountryList,
            )

            else -> null
        }
    }
}

internal class CountryListPresenter(
    private val getCountryList: IGetCountryListUseCase,
    private val updateCountryList: IUpdateCountryListUseCase,
    private val countryListConverter: ICountryListConverter
) : Presenter<CountryListScreen.CountryListState> {

    @Composable
    override fun present(): CountryListScreen.CountryListState {
        var isRefresh by remember { mutableStateOf(false) }
        var nameOfDialog: String? by remember { mutableStateOf(null) }
        if (isRefresh) {
            isRefresh = false
            LaunchedEffect(Unit) {
                runCatching { updateCountryList() }
            }
        }
        val response by produceState<RequestAction<List<CountryResponse>>>(
            initialValue = RequestAction.Progress,
            key1 = Unit,
        ) {
            subscribe { getCountryList() }.collect { value = it }
        }
        return when (val result = response) {
            is RequestAction.Data -> {
                CountryListScreen.CountryListState.Success(
                    items = countryListConverter.convert(result.data),
                    nameOfDialog = nameOfDialog,
                ) { event ->
                    when (event) {
                        is CountryListScreen.Event.CountryClick -> nameOfDialog = event.name
                        is CountryListScreen.Event.DismissCountry -> nameOfDialog = null
                        else -> Unit
                    }
                }
            }

            is RequestAction.Error -> {
                CountryListScreen.CountryListState.Error {
                    when (it) {
                        is CountryListScreen.Event.Reload -> isRefresh = true
                        else -> Unit
                    }
                }
            }

            is RequestAction.Progress -> CountryListScreen.CountryListState.Loading
            RequestAction.Empty -> CountryListScreen.CountryListState.Loading
        }
    }
}