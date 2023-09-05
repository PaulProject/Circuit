package com.example.circuit.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class TestCoroutineDispatcherProvider(
    testCoroutineDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : ICoroutineDispatcherProvider {

    override val main: CoroutineDispatcher = testCoroutineDispatcher
    override val mainImmediate: CoroutineDispatcher = testCoroutineDispatcher
    override val io: CoroutineDispatcher = testCoroutineDispatcher
    override val default: CoroutineDispatcher = testCoroutineDispatcher

    override val sessionScope: CleanableCoroutineScope = CleanableCoroutineScope(
        SupervisorJob() + io
    )
}