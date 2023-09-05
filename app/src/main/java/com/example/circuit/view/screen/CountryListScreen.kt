package com.example.circuit.view.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.circuit.view.item.CountryItem
import com.example.circuit.view.screen.dialog.CountryDialog
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
data object CountryListScreen : Screen {

    sealed interface CountryListState : CircuitUiState {
        data object Loading : CountryListState
        data class Success(
            val items: List<CountryItem> = emptyList(),
            val nameOfDialog: String? = null,
            val eventSink: (Event) -> Unit = {},
        ) : CountryListState
        data class Error(
            val eventSink: (Event) -> Unit = {},
        ): CountryListState
    }

    sealed interface Event : CircuitUiEvent {
        data object Reload: Event
        data object DismissCountry: Event
        data class CountryClick(val name: String) : Event
    }
}

class CountryListUiFactory @Inject constructor() : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? = when (screen) {
        is CountryListScreen -> {
            ui<CountryListScreen.CountryListState> { state, modifier ->
                CountryListUi(state, modifier)
            }
        }

        else -> null
    }
}

@Composable
private fun CountryListUi(state: CountryListScreen.CountryListState, modifier: Modifier) {
    Box(modifier = modifier) {
        when (state) {
            is CountryListScreen.CountryListState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(24.dp)
                )
            }
            is CountryListScreen.CountryListState.Success -> {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(state.items) { item ->
                        CountryCell(
                            item = item,
                            onCountryClick = {
                                state.eventSink(CountryListScreen.Event.CountryClick(it.name))
                            }
                        )
                    }
                }
                state.nameOfDialog?.let {
                    CountryDialog(it) {
                        state.eventSink(CountryListScreen.Event.DismissCountry)
                    }
                }
            }
            is CountryListScreen.CountryListState.Error -> {
                Button(
                    modifier = Modifier.align(Alignment.Center),
                    content = {
                        Text("Reload")
                    },
                    onClick = {
                        state.eventSink(CountryListScreen.Event.Reload)
                    }
                )
            }
        }
    }
}

@Composable
private fun CountryCell(
    item: CountryItem,
    onCountryClick: (item: CountryItem) -> Unit,
) {
    AsyncImage(
        modifier = Modifier.clickable {
            onCountryClick(item)
        },
        model = ImageRequest.Builder(LocalContext.current)
            .data(item.image)
            .decoderFactory(SvgDecoder.Factory())
            .build(),
        contentDescription = null,
    )
}