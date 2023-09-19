package com.solanamobile.krate.profilescreen.viewmodel

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.moriatsushi.koject.Provides
import com.solanamobile.krate.extensions.ApiKeys
import com.solanamobile.krate.localstorage.UserAccountUseCase
import com.solanamobile.krate.profilescreen.ProfileAuthenticator
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
class ProfileScreenViewModel(
    private val acctUseCase: UserAccountUseCase,
    val authenticator: ProfileAuthenticator
): StateScreenModel<ProfileViewState>(ProfileViewState.Default) {

    private val api = UnderdogApiV2(true)

    fun setup() {
        authenticator.init()
    }

    fun login() {
        authenticator.authenticate()
    }

    fun loadMintedNfts() {
        coroutineScope.launch {
            mutableState.update {
                ProfileViewState.Loading
            }

            val myNfts = api.listNfts(1, ApiKeys.NFT_API_KEY, acctUseCase.userAddress)

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