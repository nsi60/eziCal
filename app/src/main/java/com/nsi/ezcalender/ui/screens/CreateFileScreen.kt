package com.nsi.ezcalender.ui.screens

import android.net.Uri
import androidx.activity.compose.BackHandler
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    val scope = rememberCoroutineScope()


    Scaffold(floatingActionButton = {
        Row(
            Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom
        ) {
            FloatingActionButton(
                onClick = {
                    openDialog = true
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "c-d")
            }
        }
    }

    ) {

        Column(
            Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp)
        ) {
            LazyColumn {
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
                                        "geo:${it.geo}?q=${
                                            URLEncoder.encode(
                                                it.location.ifEmpty { "hell" },
                                                "UTF-8"
                                            )
                                        }"
                                    )
                                )

                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 12.dp),
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
                                Text(text = it.summary.toString())
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


            FullScreenAlertDialog(
                openDialog,
                saveEvent = { event, creatAnotherEvent ->
                    saveEvent(event)
                    scope.launch {
                        openDialog = false  //TODO add a checkbox for creating new event
                        if (creatAnotherEvent) {
                            delay(500)
                            openDialog = true
                        }
                    }

                },
                closeDialog = { openDialog = false }
            )

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

@Composable
fun CustomDisabledTextField(
    modifier: Modifier,
    value: String,
    startDatePickerDialog: () -> Unit,
    label: String,
    placeHolder: String,
    trailingIcon: @Composable (() -> Unit)
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                color = Color.Unspecified,
                shape = CircleShape,
            )
            .clickable {
                startDatePickerDialog()
            },
        value = value,
        onValueChange = {},
        label = { Text(label) },
        placeholder = { Text(placeHolder) },
        enabled = false,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            disabledTextColor = LocalContentColor.current.copy(LocalContentAlpha.current)
        ),
        trailingIcon = trailingIcon
    )
}