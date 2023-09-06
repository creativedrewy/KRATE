package com.solanamobile.krate.localstorage

import com.moriatsushi.koject.Provides
import com.moriatsushi.koject.Singleton

@Singleton
@Provides
class EncryptedPrefsRepository(
    private val dataStore: VaultDataStore
) {

    fun hasKeyValue(key: String): Boolean {
        return dataStore.vault.existsObject(key)
    }

    fun saveStringPref(key: String, value: String) {
        dataStore.vault.set(key, stringValue = value)
    }

    fun getStringPref(key: String): String {
        return dataStore.vault.string(key)
            ?: throw IllegalArgumentException("Could not find pref value with provided key. Please check for existence before retrieving.")
    }
}