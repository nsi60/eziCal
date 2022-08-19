package com.nsi.ezcalender.ui.screens

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.nsi.ezcalender.model.Event
import com.nsi.ezcalender.ui.MainViewModel
import com.nsi.ezcalender.ui.common.FullScreenAlertDialog
import java.net.URLEncoder

@Composable
fun CreateFileScreen(
    mainViewModel: MainViewModel,
    navigateUp: () -> Unit
) {
    val state = mainViewModel.state.value
    CreateFileScreenContent(
        createdEvents = state.createdEventsList,
        saveEvent = {
            mainViewModel.saveCreatedEvent(it)
        }
    )

    BackHandler {
        navigateUp()
    }

}


@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun CreateFileScreenContent(
    createdEvents: MutableList<Event>,
    saveEvent: (Event) -> Unit
) {

    var openDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current


    Column(Modifier.fillMaxSize()) {
        LazyColumn() {
            items(createdEvents) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .shadow(elevation = 2.dp)
//                        .animateItemPlacement()   //TODO not the best looking
                        .clickable {
                            startImplicitIntent(
                                context, Uri.parse(
                                    it.geo ?: "geo:0,0?q=${URLEncoder.encode(it.location, "UTF-8")}"
                                )
                            )

                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 12.dp),
//                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(it.dtStart?.dayOfMonth.toString())
                            Text(it.dtStart?.month.toString())
//                            Text(it.dtStart?.year.toString())

                        }
                        Column(Modifier.weight(2f)) {
                            Text(text = it.summary)
                            Text(
                                text = "${it.dtStart?.hour.toString()}:${it.dtStart?.minute.toString()} " +
                                        "- ${it.dtEnd?.hour.toString()}:${it.dtEnd?.minute.toString()}"
                            )
                            Text(text = it.location)
                        }

                    }
                }


            }
        }

//        AnimatedVisibility (
//            visible = openDialog,
//        enter = slideInVertically(),
//        exit = slideOutVertically())

        if (openDialog) {
            FullScreenAlertDialog(
                saveEvent = {
                    saveEvent(it)
                    openDialog = false  //TODO add a checkbox for creating new event
                },
                closeDialog = { openDialog = false }
            )
        }

        Row(
            Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp, end = 4.dp)
                .padding(12.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom
        ) {
            FloatingActionButton(
                onClick = {
                    openDialog = true
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        }
    }
}


@Composable
fun CustomTextField(
    text: TextFieldValue,
    updateText: (TextFieldValue) -> Unit,
    label: String,
    placeHolder: String,
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                color = Color.Unspecified,
                shape = CircleShape,
            ),
        value = text,
        onValueChange = { newText ->
            updateText(newText)
//            text = newText
        },
        label = { Text(text = label) },
        placeholder = { Text(text = placeHolder) },
        maxLines = 1
    )
}