package com.solanamobile.krate.coroutines

import com.moriatsushi.koject.Qualifier

@Qualifier
annotation class Dispatcher(val dispatcher: Dispatchers)

enum class Dispatchers {
    Main,
    IO,
}
