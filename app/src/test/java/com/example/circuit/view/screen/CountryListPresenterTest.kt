package com.example.circuit.view.screen

import com.dropbox.android.external.store4.ResponseOrigin
import com.dropbox.android.external.store4.StoreResponse
import com.example.circuit.business.usecase.IGetCountryListUseCase
import com.example.circuit.business.usecase.IUpdateCountryListUseCase
import com.example.circuit.data.response.CountryResponse
import com.example.circuit.view.converter.ICountryListConverter
import com.example.circuit.view.item.CountryItem
import com.slack.circuit.test.test
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.concurrent.TimeoutException

@RunWith(RobolectricTestRunner::class)
class CountryListPresenterTest {

    private lateinit var presenter: CountryListPresenter

    @MockK
    private lateinit var getCountryList: IGetCountryListUseCase

    @MockK
    private lateinit var updateCountryList: IUpdateCountryListUseCase

    @MockK
    private lateinit var countryListConverter: ICountryListConverter

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        presenter = CountryListPresenter(
            getCountryList = getCountryList,
            updateCountryList = updateCountryList,
            countryListConverter = countryListConverter
        )
    }

    @After
    fun tearDown() {
        confirmVerified(
            getCountryList,
            updateCountryList,
            countryListConverter,
        )
    }

    @Test
    fun getCountryList_isSuccess() = runTest {
        // mock
        val response = listOf(
            CountryResponse()
        )
        val result = listOf(
            CountryItem()
        )
        coEvery { getCountryList() } returns flowOf(
            StoreResponse.Data(
                response, ResponseOrigin.Fetcher
            )
        )
        every { countryListConverter.convert(response) } returns result

        // action
        presenter.test {
            awaitItem() shouldBe CountryListScreen.CountryListState.Loading
            val state = awaitItem()
            check(state is CountryListScreen.CountryListState.Success)
            state.items shouldBe result
        }

        // verify
        coVerify { getCountryList() }
        verify { countryListConverter.convert(response) }
    }

    @Test
    fun getCountryList_isError() = runTest {
        // mock
        val error = TimeoutException()
        coEvery { getCountryList() } returns flowOf(
            StoreResponse.Error.Exception(error, ResponseOrigin.Fetcher)
        )

        // action
        presenter.test {
            awaitItem() shouldBe CountryListScreen.CountryListState.Loading
            val state = awaitItem()
            check(state is CountryListScreen.CountryListState.Error)
        }

        // verify
        coVerify { getCountryList() }
    }

    @Test
    fun onCountryClick() = runTest {
        // mock
        val response = listOf(
            CountryResponse()
        )
        val result = listOf(
            CountryItem()
        )
        coEvery { getCountryList() } returns flowOf(
            StoreResponse.Data(
                response, ResponseOrigin.Fetcher
            )
        )
        every { countryListConverter.convert(response) } returns result

        // action
        presenter.test {
            awaitItem() shouldBe CountryListScreen.CountryListState.Loading
            awaitItem().run {
                check(this is CountryListScreen.CountryListState.Success)
                eventSink(CountryListScreen.Event.CountryClick("country"))
            }
            awaitItem().run {
                check(this is CountryListScreen.CountryListState.Success)
                nameOfDialog shouldBe "country"
            }
        }

        // verify
        coVerify { getCountryList() }
        verify { countryListConverter.convert(response) }
    }
}