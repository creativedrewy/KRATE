package com.solanamobile.krate.localstorage

import android.content.Context
import com.liftric.kvault.KVault
import com.moriatsushi.koject.Provides

@Provides
actual class VaultDataStore(
    val context: Context
) {
    actual val vault: KVault = KVault(context, "user_account")
}