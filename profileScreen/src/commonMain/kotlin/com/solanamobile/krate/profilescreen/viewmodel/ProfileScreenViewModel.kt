package com.solanamobile.krate.profilescreen.viewmodel

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.moriatsushi.koject.Provides
import com.solanamobile.krate.extensions.ApiKeys
import com.underdogprotocol.api.UnderdogApiV2
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class ProfileViewState {
    object Default: ProfileViewState()

    object Loading: ProfileViewState()

    data class Loaded(
        val images: List<String> = listOf()
    ): ProfileViewState()
}

@Provides
class ProfileScreenViewModel: StateScreenModel<ProfileViewState>(ProfileViewState.Default) {

    private val api = UnderdogApiV2(true)

    fun loadMintedNfts() {
        coroutineScope.launch {
            mutableState.update {
                ProfileViewState.Loading
            }

            val myNfts = api.listNfts(1, ApiKeys.NFT_API_KEY, "i5Ww8XokvATpEL8xmu8uXQhjSQMGzgHeB9N8VSDzX3p")

            mutableState.update {
                ProfileViewState.Loaded(
                    images = myNfts.results.map {
                        it.image
                    }
                )
            }
        }
    }

}