package com.solanamobile.krate.localstorage

import com.liftric.kvault.KVault

expect class VaultDataStore {
    val vault: KVault
}