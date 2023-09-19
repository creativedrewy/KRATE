package com.solanamobile.krate.profilescreen

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.moriatsushi.koject.Provides
import com.solanamobile.krate.profilescreen.viewmodel.ProfileScreenViewModel

@Composable
actual fun configureScreen(vm: ProfileScreenViewModel) {
    val activity = (LocalContext.current as Activity)

    Helper.activity = activity
}

/**
 * TODO: THIS IS BAD ARCHITECTURE FIX THIS EVENTUALLY
 */
object Helper {
    var activity: Activity? = null
}

@Provides
actual class ProfileAuthenticator() {

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    actual fun init() {
        oneTapClient = Identity.getSignInClient(Helper.activity!!)

        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                .setSupported(true)
                .build())
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId("ASERVERCLIENTID")
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .setAutoSelectEnabled(true)
            .build()
    }

    actual fun authenticate() {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                Helper.activity!!.startIntentSenderForResult(result.pendingIntent.intentSender, 2, null, 0, 0, 0, null)

                Log.d("Andrew", "Login attempt successful")
            }
            .addOnFailureListener { e ->
                Log.e("Andrew", e.message, e)
            }
    }

}