package com.solanamobile.krate.profilescreen.viewmodel

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.moriatsushi.koject.Provides
import com.underdogprotocol.api.UnderdogApiV2
import kotlinx.coroutines.launch

data class ProfileViewState(
    val images: List<String> = listOf()
)

@Provides
class ProfileScreenViewModel: StateScreenModel<ProfileViewState>(ProfileViewState()) {

    private val api = UnderdogApiV2(true)

    fun loadMintedNfts() {
        coroutineScope.launch {
//            val myNfts = api.listNfts(1, ApiKeys.NFT_API_KEY, "i5Ww8XokvATpEL8xmu8uXQhjSQMGzgHeB9N8VSDzX3p")
//
//            mutableState.update {
//                ProfileViewState(
//                    images = myNfts.results.map {
//                        it.image
//                    }
//                )
//            }

//            myNfts.results.forEach { nft ->
//                Logger.v(tag = "Andrew") { "Name: ${nft.name}, address: ${nft.mintAddress}, img: ${nft.image}" }
//            }
        }
    }

}