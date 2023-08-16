package com.solanamobile.krate.extension

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.moriatsushi.koject.Qualifier
import com.moriatsushi.koject.compose.rememberInject

@Composable
public inline fun <reified T : ScreenModel> Screen.getScreenModel(
    qualifier: Qualifier? = null,
): T {
    val viewModel: T = rememberInject()

    return rememberScreenModel(tag = qualifier?.toString()) { viewModel }
}