package com.solanamobile.krate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var oneTapClient: SignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        oneTapClient = Identity.getSignInClient(this)

        setContent {
            val systemUiController = rememberSystemUiController()
            val isDarkMode = isSystemInDarkTheme()

            SideEffect {
                systemUiController.setStatusBarColor(
                    color = Color.Transparent,
                    darkIcons = !isDarkMode
                )

                systemUiController.setNavigationBarColor(
                    Color.Transparent,
                    darkIcons = !isDarkMode
                )
            }

            MainView()
        }
    }

    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
    private var showOneTapUI = true

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val auth = Firebase.auth

        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken

                    Log.v("Andrew", "::: ${credential.displayName}")
                    Log.v("Andrew", "::: ${credential.id}")
                    Log.v("Andrew", "::: ${credential.familyName}")
                    Log.v("Andrew", "::: ${credential.givenName}")
                    Log.v("Andrew", "::: ${credential.password}")
                    Log.v("Andrew", "::: ${credential.profilePictureUri}")

                    when {
                        idToken != null -> {
                            //Got an ID token from Google. Use it to authenticate with Firebase.
                            Log.d("Andrew", "::: Your ID Token: $idToken")

                            val cred = GoogleAuthProvider.getCredential(idToken,null)
                            Log.d("Andrew", "::: Your Cred: ${cred.provider}")
                            Log.d("Andrew", "::: Your Cred: ${cred.signInMethod}")
                        }

                        else -> {
                            Log.d("Andrew", "No ID token!")
                        }
                    }
                } catch (e: ApiException) {
                    Log.e("Andrew", "Error authenticating", e)
                }
            }
        }
    }
}