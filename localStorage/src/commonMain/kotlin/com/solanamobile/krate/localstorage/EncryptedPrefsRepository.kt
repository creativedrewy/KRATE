package com.solanamobile.krate.localstorage

import com.moriatsushi.koject.Provides
import com.moriatsushi.koject.Singleton

@Singleton
@Provides
class EncryptedPrefsRepository(
    private val dataStore: VaultDataStore
) {

    fun hasKeyValue(key: String): Boolean {
        return dataStore.settings.hasKey(key)
    }

    fun saveStringPref(key: String, value: String) {
        dataStore.settings.putString(key, value)
    }

    fun getStringPref(key: String): String {
        return dataStore.settings.getString(key, "")
    }
}