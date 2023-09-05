package com.example.circuit.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject

interface ICoroutineDispatcherProvider {

    val main: CoroutineDispatcher
    val mainImmediate: CoroutineDispatcher

    val io: CoroutineDispatcher
    val default: CoroutineDispatcher

    val sessionScope: ICleanableCoroutineScope
}

internal class CoroutineDispatcherProvider @Inject constructor() : ICoroutineDispatcherProvider {

    override val main: CoroutineDispatcher by lazy { Dispatchers.Main }
    override val mainImmediate: CoroutineDispatcher by lazy { Dispatchers.Main.immediate }
    override val io: CoroutineDispatcher by lazy { Dispatchers.IO }
    override val default: CoroutineDispatcher by lazy { Dispatchers.Default }

    override val sessionScope: ICleanableCoroutineScope = CleanableCoroutineScope(
        SupervisorJob() + io
    )
}