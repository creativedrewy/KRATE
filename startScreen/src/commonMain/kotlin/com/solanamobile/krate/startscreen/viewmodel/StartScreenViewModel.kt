package com.solanamobile.krate.startscreen.viewmodel

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import co.touchlab.kermit.Logger
import com.moriatsushi.koject.Provides
import foundation.metaplex.solanaeddsa.SolanaEddsa
import kotlinx.coroutines.launch

data class StartScreenViewState(
    val loaded: Boolean = true
)

@Provides
class StartScreenViewModel: StateScreenModel<StartScreenViewState>(StartScreenViewState()) {

    init {
        coroutineScope.launch {
            val blah = SolanaEddsa.generateKeypair()

            Logger.v { "Your Pubkey: ${blah.publicKey}" }
        }
    }

}