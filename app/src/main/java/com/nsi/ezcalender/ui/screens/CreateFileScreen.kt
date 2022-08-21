package com.nsi.ezcalender.ui.screens

import EventComposable
import EzIcalLogoGif
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.nsi.ezcalender.MainViewModel
import com.nsi.ezcalender.R
import com.nsi.ezcalender.model.Event
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

    var openCreateEventDialog by rememberSaveable { mutableStateOf(false) }
    var openViewEventDialog by rememberSaveable { mutableStateOf(false) }


    CreateFileScreenContent(
        openViewEventDialog = openViewEventDialog,
        openCreateEventDialog = openCreateEventDialog,
        createdEvents = state.createdEventsList,
        saveEvent = {
            mainViewModel.saveCreatedEvent(it)
        },
        deleteEvent = {
            mainViewModel.deleteEvent(it)
        },
        exportCalender = {
            mainViewModel.exportCreatedEvents(context.filesDir)
        },
        updateCreateEventDialog = {
            openCreateEventDialog = it
        },
        updateViewEventDialog = {
            openViewEventDialog = it
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
    openViewEventDialog: Boolean,
    openCreateEventDialog: Boolean,
    createdEvents: SnapshotStateList<Event>,
    saveEvent: (Event) -> Unit,
    deleteEvent: (Event) -> Unit,
    exportCalender: () -> Unit,
    updateViewEventDialog: (Boolean) -> Unit,
    updateCreateEventDialog: (Boolean) -> Unit,

    ) {


    val context = LocalContext.current
    val scope = rememberCoroutineScope()
//    var selectedEvent: Event? by rememberSaveable { mutableStateOf(createdEvents.ifEmpty { null }?.get(0)) } //TODO crashes  on rotate
    var selectedEvent: Int? by rememberSaveable { mutableStateOf(if (createdEvents.isEmpty()) null else 0) }


    Scaffold(floatingActionButton = {
        Row(
            Modifier
                .fillMaxSize(),
//                .padding(bottom = 60.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom
        ) {
            FloatingActionButton(
                onClick = {
                    updateCreateEventDialog(true)
//                    openCreateEventDialog = true
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "c-d")
            }
        }
    }

    ) {

        CreateEventDialog(
            openCreateEventDialog,
            saveEvent = { event, createAnotherEvent ->
                saveEvent(event)
                scope.launch {
                    updateCreateEventDialog(false)
//                    openCreateEventDialog = false  //TODO add a checkbox for creating new event
                    if (createAnotherEvent) {
                        delay(500)
                        updateCreateEventDialog(true)
//                        openCreateEventDialog = true
                    }
                }

            },
            closeDialog = {
                updateCreateEventDialog(false)
//                openCreateEventDialog = false
            },
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

                    EzIcalLogoGif(
                        Modifier
                            .scale(0.5f)
                            .weight(1f)
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        text = stringResource(id = R.string.createEventNoContentDescription),
                        textAlign = TextAlign.Center
                    )

                }

            }

            if (openViewEventDialog) {
                ViewEventDialog(
                    event = createdEvents[selectedEvent!!],
                    openDialog = openViewEventDialog,
                    closeDialog = {
                        updateViewEventDialog(false)
//                        openViewEventDialog = false
                    }
                )
            }

            LazyColumn {

                itemsIndexed(items = createdEvents, key = { _, event -> event.uid!! }
                ) { index, event ->
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
                                    selectedEvent = index //it
                                    updateViewEventDialog(true)
//                                    openViewEventDialog = true
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