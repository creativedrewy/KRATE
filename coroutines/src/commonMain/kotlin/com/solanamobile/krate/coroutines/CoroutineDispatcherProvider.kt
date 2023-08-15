package com.solanamobile.krate.coroutines

import com.moriatsushi.koject.Provides
import kotlinx.coroutines.CoroutineDispatcher

internal expect object CoroutineDispatcherProvider {
    @Dispatcher(Dispatchers.Main)
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher

    @Dispatcher(Dispatchers.IO)
    @Provides
    fun provideIODispatcher(): CoroutineDispatcher
}