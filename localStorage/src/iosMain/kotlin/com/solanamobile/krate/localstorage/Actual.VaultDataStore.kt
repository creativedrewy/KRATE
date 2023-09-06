package com.solanamobile.krate.localstorage

import com.liftric.kvault.KVault
import com.moriatsushi.koject.Provides

@Provides
actual class VaultDataStore {
    actual val vault: KVault = KVault("user_account", "krate")
}