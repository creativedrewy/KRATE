package com.solanamobile.krate.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal actual object CoroutineDispatcherProvider {
    actual fun provideIODispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    actual fun provideMainDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main.immediate
    }
}
