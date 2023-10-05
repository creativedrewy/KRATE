package com.solanamobile.krate.profilescreen.viewmodel

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import co.touchlab.kermit.Logger
import com.moriatsushi.koject.Provides
import com.solanamobile.krate.extensions.ApiKeys
import com.solanamobile.krate.kratedb.repository.UserStorageRepository
import com.solanamobile.krate.localstorage.UserAccountUseCase
import com.solanamobile.krate.profilescreen.ProfileAuthenticator
import com.underdogprotocol.api.UnderdogApiV2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * This isn't full profile state anymore; it's more the user nft load state
 */
sealed class ProfileViewState {
    object Default: ProfileViewState()

    object Loading: ProfileViewState()

    data class Loaded(
        val images: List<String> = listOf()
    ): ProfileViewState()
}

/**
 * This class provides the "true" status of the user's profile
 */
sealed class AuthViewState {
    object NotLoggedIn: AuthViewState()

    class LoggedIn(
        val name: String,
        val imageUrl: String
    ): AuthViewState()
}

@Provides
class ProfileScreenViewModel(
    private val acctUseCase: UserAccountUseCase,
    private val userStorageRepository: UserStorageRepository,
    val authenticator: ProfileAuthenticator
): StateScreenModel<ProfileViewState>(ProfileViewState.Default) {

    private val api = UnderdogApiV2(true)

    val authState: Flow<AuthViewState> =
        userStorageRepository.loggedInUser
            .map { user ->
                Logger.v { "::: Your user: $user" }

                user?.let {
                    Logger.v { "::: YOU ARE LOGGED IN AND STUFF" }

                    AuthViewState.LoggedIn(
                        name = it.displayName,
                        imageUrl = it.imageUrl
                    )
                } ?: AuthViewState.NotLoggedIn
            }

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