package com.solanamobile.krate.localstorage

import com.moriatsushi.koject.Provides
import foundation.metaplex.solanaeddsa.SolanaEddsa
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Provides
class UserAccountUseCase(
    private val prefsRepo: EncryptedPrefsRepository
) {

    val userAddress: String
        get() = prefsRepo.getStringPref(accountKeyName)

    fun accountExists(): Boolean {
        return prefsRepo.hasKeyValue(accountKeyName) && prefsRepo.hasKeyValue(secretKeyName)
    }

    @OptIn(ExperimentalEncodingApi::class)
    suspend fun createUserAccount() {
        val keypair = SolanaEddsa.generateKeypair()

        val pubkey = keypair.publicKey.toBase58()
        val secret = Base64.encode(keypair.secretKey)

        prefsRepo.saveStringPref(accountKeyName, pubkey)
        prefsRepo.saveStringPref(secretKeyName, secret)
    }

    companion object {
        const val accountKeyName = "user_account"
        const val secretKeyName = "user_secret"
    }
}