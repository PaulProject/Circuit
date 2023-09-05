package com.example.circuit.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlin.coroutines.CoroutineContext

interface ICleanableCoroutineScope : CoroutineScope {

    fun clear()
}

class CleanableCoroutineScope(private val context: CoroutineContext) : ICleanableCoroutineScope {

    override val coroutineContext: CoroutineContext get() = context

    override fun clear() {
        coroutineContext.cancelChildren()
    }
}