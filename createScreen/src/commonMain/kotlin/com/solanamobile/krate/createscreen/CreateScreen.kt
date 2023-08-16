package com.solanamobile.krate.createscreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import com.solanamobile.krate.createscreen.viewmodel.CreateScreenViewModel
import com.solanamobile.krate.extension.getScreenModel

class CreateScreen: Screen {

    @Composable
    override fun Content() {
        val viewModel: CreateScreenViewModel = getScreenModel()
        val state by viewModel.state.collectAsState()

        CreateScreenContent(state)
    }
}

@Composable
fun CreateScreenContent(
    state: String
) {

}