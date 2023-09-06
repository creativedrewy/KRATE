package com.solanamobile.krate.localstorage

import co.touchlab.kermit.Logger
import com.moriatsushi.koject.Provides
import com.moriatsushi.koject.Singleton

@Singleton
@Provides
class EncryptedPrefsRepository(
    private val dataStore: VaultDataStore
) {

    fun savePref(name: String, value: String) {
        Logger.v { "Yup, you can do this ${ dataStore.vault.allKeys() }" }
    }

}