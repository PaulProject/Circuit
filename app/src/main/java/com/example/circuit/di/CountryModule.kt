package com.example.circuit.di

import com.example.circuit.business.usecase.GetCountryListUseCase
import com.example.circuit.business.usecase.IGetCountryListUseCase
import com.example.circuit.business.usecase.IUpdateCountryListUseCase
import com.example.circuit.business.usecase.UpdateCountryListUseCase
import com.example.circuit.data.repository.CountryRepository
import com.example.circuit.data.repository.ICountryRepository
import com.example.circuit.data.service.CountryService
import com.example.circuit.view.converter.CountryListConverter
import com.example.circuit.view.converter.ICountryListConverter
import com.example.circuit.view.screen.CountryListPresenterFactory
import com.example.circuit.view.screen.CountryListUiFactory
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal interface CountryBindModule {

    @Singleton
    @Binds
    fun bindCountryRepository(impl: CountryRepository): ICountryRepository

    @Singleton
    @Binds
    fun bindGetCountryListUseCase(impl: GetCountryListUseCase): IGetCountryListUseCase

    @Singleton
    @Binds
    fun bindUpdateCountryListUseCase(impl: UpdateCountryListUseCase): IUpdateCountryListUseCase

    @Singleton
    @Binds
    fun bindCountryListConverter(impl: CountryListConverter): ICountryListConverter

    @Binds
    @IntoSet
    fun bindCountryListPresenterFactory(impl: CountryListPresenterFactory): Presenter.Factory

    @Binds
    @IntoSet
    fun bindCountryListUiFactory(impl: CountryListUiFactory): Ui.Factory
}

@InstallIn(SingletonComponent::class)
@Module
internal class CountryProvideModule {

    @Provides
    fun provideCountryService(retrofit: Retrofit): CountryService =
        retrofit.create(CountryService::class.java)

}