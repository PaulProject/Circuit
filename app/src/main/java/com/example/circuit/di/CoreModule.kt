package com.example.circuit.di

import com.example.circuit.coroutine.CoroutineDispatcherProvider
import com.example.circuit.coroutine.ICoroutineDispatcherProvider
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal class CoreProvideModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient()
            .newBuilder()
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .build()

    @Provides
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    fun provideRetrofit(
        client: OkHttpClient,
        moshi: Moshi
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://cdn.jsdelivr.net/npm/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

}

@InstallIn(SingletonComponent::class)
@Module
internal interface CoreBindModule {

    @Binds
    @Singleton
    fun bindCoroutineDispatcherProvider(impl: CoroutineDispatcherProvider): ICoroutineDispatcherProvider

}