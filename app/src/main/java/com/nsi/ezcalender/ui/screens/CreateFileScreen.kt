package com.nsi.ezcalender.ui.screens

import EventComposable
import EzIcalLogoGif
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nsi.ezcalender.R
import com.nsi.ezcalender.model.Event
import com.nsi.ezcalender.ui.MainViewModel
import com.nsi.ezcalender.ui.common.CreateEventDialog
import com.nsi.ezcalender.ui.common.ViewEventDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CreateFileScreen(
    mainViewModel: MainViewModel,
    navigateUp: () -> Unit
) {
    val state = mainViewModel.state.value
    val context = LocalContext.current
    CreateFileScreenContent(
        createdEvents = state.createdEventsList,
        saveEvent = {
            mainViewModel.saveCreatedEvent(it)
        },
        deleteEvent = {
            mainViewModel.deleteEvent(it)
        },
        exportCalender = {
            mainViewModel.exportCreatedEvents(context.filesDir)
        }
    )

    BackHandler {
        navigateUp()
    }

}


@OptIn(
    ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun CreateFileScreenContent(
    createdEvents: SnapshotStateList<Event>,
    saveEvent: (Event) -> Unit,
    deleteEvent: (Event) -> Unit,
    exportCalender: () -> Unit

) {

    var openDialog by remember { mutableStateOf(false) }
    var openViewEventDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var selectedEvent: Event? by remember { mutableStateOf(null) }


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

        CreateEventDialog(
            openDialog,
            saveEvent = { event, createAnotherEvent ->
                saveEvent(event)
                scope.launch {
                    openDialog = false  //TODO add a checkbox for creating new event
                    if (createAnotherEvent) {
                        delay(500)
                        openDialog = true
                    }
                }

            },
            closeDialog = { openDialog = false },
        )



        Column(
            Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp)
        ) {

            if (createdEvents.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = {
                        exportCalender()
                    }) {
                        Text(stringResource(id = R.string.exportOrSaveText))
                    }
                }
                Divider()
            } else {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    EzIcalLogoGif(Modifier.scale(0.5f))
                    Text(
                        stringResource(id = R.string.createEventNoContentDescription),
                        textAlign = TextAlign.Center
                    )

                }

            }

            ViewEventDialog(
                event = selectedEvent,
                openDialog = openViewEventDialog,
                closeDialog = { openViewEventDialog = false }
            )

            LazyColumn {

                items(items = createdEvents, key = { it.uid!! }
                ) { event ->
                    val dismissState = rememberDismissState(
                        confirmStateChange = {
                            if (it == DismissValue.DismissedToStart) {
                                deleteEvent(event)
                            }
                            true
                        }
                    )

                    SwipeToDismiss(
                        state = dismissState,
                        directions = setOf(DismissDirection.EndToStart),
                        background = {

                            val color = when (dismissState.dismissDirection) {
                                DismissDirection.EndToStart -> Color.Black
                                DismissDirection.StartToEnd -> Color.Gray
                                null -> Color.Transparent
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color = color)
                                    .padding(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete, contentDescription = "c-d",
                                    tint = Color.Gray,
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                )
                            }
                        },
                        dismissContent = {
                            EventComposable(
                                modifier = Modifier.animateItemPlacement(),
                                context = context,
                                event = event,
                                onClick = {
                                    selectedEvent = it
                                    openViewEventDialog = true
                                })
                        },

                        )
                }
            }

//        AnimatedVisibility (
//            visible = openDialog,
//        enter = slideInVertically(),
//        exit = slideOutVertically())


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
    modifier: Modifier = Modifier,
    value: String,
    onClick: () -> Unit,
    label: String,
    placeHolder: String = "",
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
                onClick()
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