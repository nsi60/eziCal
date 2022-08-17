package com.nsi.ezcalender.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.nsi.ezcalender.model.Event
import com.nsi.ezcalender.ui.MainViewModel

@Composable
fun ReadFileScreen(
    mainViewModel: MainViewModel,
    navigateUp: () -> Unit
) {
    LaunchedEffect(Unit) {
        mainViewModel.readSelectedFile()
    }

    val events = mainViewModel.state.value.eventsList

    ReadFileScreenContent(events)

    BackHandler {
        navigateUp()
    }
}


@Composable
fun ReadFileScreenContent(events: List<Event>) {
    Column(modifier = Modifier.fillMaxSize()) {

        LazyColumn() {
            items(events) {
                Text(text = it.summary)
            }
        }
    }


}