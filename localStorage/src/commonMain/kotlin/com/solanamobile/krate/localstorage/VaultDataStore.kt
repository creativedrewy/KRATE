package com.solanamobile.krate.localstorage

import com.moriatsushi.koject.Provides
import com.russhwolf.settings.Settings

@Provides
class VaultDataStore {
    val settings = Settings()
}