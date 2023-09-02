package com.solanamobile.krate.profilescreen.viewmodel

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import co.touchlab.kermit.Logger
import com.moriatsushi.koject.Provides
import com.solanamobile.krate.extensions.ApiKeys
import com.underdogprotocol.api.UnderdogApiV2
import kotlinx.coroutines.launch

data class ProfileViewState(
    val name: String = "Name"
)

@Provides
class ProfileScreenViewModel: StateScreenModel<ProfileViewState>(ProfileViewState()) {

    private val api = UnderdogApiV2(true)

    fun loadMintedNfts() {
        coroutineScope.launch {
            val myNfts = api.listNfts(1, ApiKeys.NFT_API_KEY, "i5Ww8XokvATpEL8xmu8uXQhjSQMGzgHeB9N8VSDzX3p")

            myNfts.results.forEach { nft ->
                Logger.v(tag = "Andrew") { "Name: ${nft.name}, address: ${nft.mintAddress}, img: ${nft.image}" }
            }
        }
    }

}