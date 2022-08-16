package com.nsi.ezcalender.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import com.nsi.ezcalender.ui.MainViewModel

@Composable
fun CreateFileScreen(
    mainViewModel: MainViewModel,
    navigateUp: () -> Unit
) {

    CreateFileScreenContent()

    BackHandler {
        navigateUp()
    }

}


@Composable
fun CreateFileScreenContent() {
}