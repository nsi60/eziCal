package com.nsi.ezcalender.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nsi.ezcalender.ui.MainViewModel
import net.fortuna.ical4j.model.Component

@Composable
fun HomeScreen(
    mainVewModel: MainViewModel,
    navigateToReadFileScreen: () -> Unit,
    navigateToCreateFileScreen: () -> Unit

) {
    val state = mainVewModel.state

    HomeScreenContent(
        openFileClicked = {
                          navigateToReadFileScreen()
                          },
        createNewFileClicked = {
            navigateToCreateFileScreen()
        }
    )

}

@Preview(showSystemUi = true)
@Composable
fun HomeScreenContent(
    openFileClicked: () -> Unit = {},
    createNewFileClicked: () -> Unit = {}

) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            openFileClicked()
        }) {
            Text(text = "Open existing file")
        }

        Button(onClick = {
            createNewFileClicked()
        }) {
            Text(text = "Create new file")
        }
    }
}

