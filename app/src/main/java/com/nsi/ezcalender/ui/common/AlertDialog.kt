package com.nsi.ezcalender.ui.common

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.nsi.ezcalender.R
import com.nsi.ezcalender.model.DateTimePickerType
import com.nsi.ezcalender.model.Event
import com.nsi.ezcalender.ui.screens.CustomDisabledTextField
import com.nsi.ezcalender.ui.screens.CustomTextField
import com.nsi.ezcalender.ui.screens.startImplicitIntent
import java.net.URLEncoder
import java.time.LocalDateTime
import java.util.*

@Composable
fun LoadingAlertDialog() {
    AlertDialog(
        onDismissRequest = {
//            openDialog.value = false
        },
        title = {
            Text(text = stringResource(id = R.string.loadingTitle))
        },
        text = {
            Text(
                stringResource(id = R.string.loadingDescription)
            )
        },
        buttons = {
            Row(
                modifier = Modifier.padding(all = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
//                        openDialog.value = false
                    }
                ) {
                    Text(stringResource(id = R.string.loadingTitle))
                }
            }
        }
    )

}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateEventDialog(
    openDialog: Boolean,
    closeDialog: () -> Unit,
    saveEvent: (Event, Boolean) -> Unit

) {
    val context = LocalContext.current
    var createAnotherEventCheck by rememberSaveable { mutableStateOf(true) }


    var dateTimePickerType by rememberSaveable { mutableStateOf(DateTimePickerType.None) }


    var summaryText by remember { mutableStateOf(TextFieldValue("")) }

    var dateStartObject by rememberSaveable { mutableStateOf(LocalDateTime.now()) }
    var dateEndObject by rememberSaveable { mutableStateOf(dateStartObject.plusHours(1)) }

    var locationText by remember { mutableStateOf(TextFieldValue("")) }
    var geoText by remember { mutableStateOf(TextFieldValue("")) }
    var urlText by remember { mutableStateOf(TextFieldValue("")) }


    var timeStartText by remember { mutableStateOf("${dateStartObject.hour} : ${dateStartObject.minute}") }
    var timeEndText by remember { mutableStateOf("${dateEndObject.hour} : ${dateEndObject.minute}") }

    var dateStartText by remember { mutableStateOf("${dateStartObject.dayOfMonth}/${dateStartObject.monthValue}/${dateStartObject.year}") }
//    var dateEndText by remember { mutableStateOf("") }
    var dateEndText by remember { mutableStateOf(dateStartText) }  //as long as end date is disabled

    val startTimePickerDialog =
        remember {
            TimePickerDialog(
                context,
                { _: TimePicker, hour: Int, minute: Int ->
                    timeStartText = "$hour : $minute"
//                dateStartText = "$dateStartText $timeStartText"
                    dateStartObject = LocalDateTime.of(
                        dateStartObject.year,
                        dateStartObject.month,
                        dateStartObject.dayOfMonth,
                        hour,
                        minute
                    )
//                showStartTimePickerDialog = false
                    dateTimePickerType = DateTimePickerType.None

                },
                dateStartObject.hour,
                dateStartObject.minute,
                false
            )
        }


    val endTimePickerDialog =
        remember {
            TimePickerDialog(
                context,
                { _: TimePicker, hour: Int, minute: Int ->
                    timeEndText = "$hour : $minute"
//                dateEndText = "$dateEndText $timeEndText"
                    dateEndObject = LocalDateTime.of(
                        dateEndObject.year,
                        dateEndObject.month,
                        dateEndObject.dayOfMonth,
                        hour,
                        minute
                    )
//                showEndTimePickerDialog = false
                    dateTimePickerType = DateTimePickerType.None
                },
                dateEndObject.hour,
                dateEndObject.minute,
                false
            )
        }


    val startDatePickerDialog = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                dateStartText = "$dayOfMonth/${month + 1}/$year"
                dateEndText = dateStartText //as long as end date is disabled
//                startTimePickerDialog.show()
                dateStartObject = LocalDateTime.of(
                    year,
                    month + 1,
                    dayOfMonth,
                    dateStartObject.hour,
                    dateStartObject.minute
                )
                dateEndObject = dateStartObject  //as long as end date is disabled
//                showStartDatePickerDialog = false
                dateTimePickerType = DateTimePickerType.None
            },
            dateStartObject.year,
            dateStartObject.monthValue,
            dateStartObject.dayOfMonth,
        )
    }


//    val endDatePickerDialog by remember {
//        DatePickerDialog(
//            context,
//            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
//                dateEndText = "$dayOfMonth/${month + 1}/$year"
////                endTimePickerDialog.show()
//                dateEndObject = LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0)
////                showEndDatePickerDialog = false
////                dateTimePickerType = DateTimePickerType.None
//            },
//            dateEndObject.year,
//            dateEndObject.monthValue,
//            dateEndObject.dayOfMonth,
//        )
//    }

    startTimePickerDialog.setOnCancelListener {
        dateTimePickerType = DateTimePickerType.None
    }

    endTimePickerDialog.setOnCancelListener {
        dateTimePickerType = DateTimePickerType.None
    }
    startDatePickerDialog.setOnCancelListener {
        dateTimePickerType = DateTimePickerType.None
    }
//    endDatePickerDialog.setOnCancelListener {
//        dateTimePickerType = DateTimePickerType.None
//    }

    LaunchedEffect(dateTimePickerType) {
        when (dateTimePickerType) {
            DateTimePickerType.START_DATE -> startDatePickerDialog.show()
//                DateTimePickerType.END_DATE ->
            DateTimePickerType.START_TIME -> startTimePickerDialog.show()
            DateTimePickerType.END_TIME -> endTimePickerDialog.show()
        }
    }


    if (openDialog) {
        Dialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = {}
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .animateContentSize()
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween
                ) {


                    Column {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = {
                                closeDialog()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "c-d"
                                )
                            }
                            Text(text = stringResource(id = R.string.createdEventTitle))

                        }
                        Divider()
                    }


                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .weight(weight = 1f, fill = false)
                    ) {
                        CustomTextField(
                            text = summaryText,
                            updateText = { summaryText = it },
                            label = stringResource(id = R.string.eventSummaryLabel),
                            placeHolder = stringResource(id = R.string.eventSummaryPlaceholder)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {

                            CustomDisabledTextField(
                                modifier = Modifier.weight(3f),
                                value = dateStartText,
                                onClick = {
                                    dateTimePickerType = DateTimePickerType.START_DATE
                                },
                                label = stringResource(id = R.string.eventStartDateLabel),
                                placeHolder = stringResource(id = R.string.eventDatePlaceholder),
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = "c-d"
                                    )
                                }
                            )

                            CustomDisabledTextField(
                                modifier = Modifier.weight(2f),
                                value = timeStartText,
                                onClick = {
                                    dateTimePickerType = DateTimePickerType.START_TIME
                                },
                                label = stringResource(id = R.string.eventStartTimeLabel),
                                placeHolder = stringResource(id = R.string.eventTimePlaceholder),
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "c-d"
                                    )
                                }
                            )
                        }


                        //TODO maybe keep end date same as start date now and only change time

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            CustomDisabledTextField(
                                modifier = Modifier.weight(3f),
                                value = dateEndText,
                                onClick = {
//                                    dateTimePickerType = DateTimePickerType.END_DATE //Todo have disabled it for now, handling same day events
                                },
                                label = stringResource(id = R.string.eventEndDateLabel),
                                placeHolder = stringResource(id = R.string.eventDatePlaceholder),
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = "c-d"
                                    )
                                }
                            )

                            CustomDisabledTextField(
                                modifier = Modifier.weight(2f),
                                value = timeEndText,
                                onClick = {
                                    dateTimePickerType = DateTimePickerType.END_TIME
                                },
                                label = stringResource(id = R.string.eventEndTimeLabel),
                                placeHolder = stringResource(id = R.string.eventTimePlaceholder),
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "c-d"
                                    )
                                }
                            )
                        }


                        CustomTextField(
                            text = locationText,
                            updateText = { locationText = it },
                            label = stringResource(id = R.string.eventLocationLabel),
                            placeHolder = stringResource(id = R.string.eventLocationPlaceholder)
                        )

                        CustomTextField(
                            text = geoText,
                            updateText = { geoText = it },
                            label = stringResource(id = R.string.eventGeoTagLabel),
                            placeHolder = stringResource(id = R.string.eventGeoTagPlaceholder)
                        )

                        CustomTextField(
                            text = urlText,
                            updateText = { urlText = it },
                            label = stringResource(id = R.string.eventUrlLabel),
                            placeHolder = stringResource(id = R.string.eventUrlPlaceholder)
                        )
                    }

                    Column {
                        Divider()
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {


                            Row(
                                Modifier.clickable {
                                    createAnotherEventCheck = !createAnotherEventCheck
                                },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = createAnotherEventCheck,
                                    onCheckedChange = {
                                        createAnotherEventCheck = it
                                    },
                                )
                                Text(stringResource(id = R.string.createAnotherEventText))
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    modifier = Modifier
                                        .padding(horizontal = 4.dp),
                                    onClick = {
                                        closeDialog()
                                    }) {
                                    Text(text = stringResource(id = R.string.cancelText))
                                }

                                Button(modifier = Modifier
                                    .padding(horizontal = 4.dp),
                                    onClick = {
                                        val event = Event(
                                            summary = summaryText.text.ifEmpty { null },
                                            dtStart = dateStartObject, //dateStartText.text,
                                            dtEnd = dateEndObject, //dateEndText.text,
                                            location = locationText.text.ifEmpty { "" },
                                            geo = geoText.text.ifEmpty { "0;0" },
                                            url = urlText.text.ifEmpty { "link.com" }
                                        )
                                        saveEvent(event, createAnotherEventCheck)
                                    }) {
                                    Text(text = stringResource(id = R.string.saveText))
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ViewEventDialog(
    event: Event?,
    openDialog: Boolean,
    closeDialog: () -> Unit,
) {
    val context = LocalContext.current

    if (openDialog) {
        Dialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = {}
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .animateContentSize()
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {


                    Column {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = {
                                closeDialog()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "c-d"
                                )
                            }
                        }
                        Divider()
                    }


                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .weight(weight = 1f, fill = false)
                    ) {
                        CustomDisabledTextField(
                            value = event?.summary.toString(),
                            onClick = { },
                            label = stringResource(id = R.string.eventSummaryLabel),
                            trailingIcon = { }
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {

                            CustomDisabledTextField(
                                modifier = Modifier.weight(3f),
                                value = "${event?.dtStart?.dayOfMonth}/${event?.dtStart?.monthValue}/${event?.dtStart?.year}",
                                onClick = {
                                    //Todo open calendar?
                                },
                                label = stringResource(id = R.string.eventStartDateLabel),
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = "c-d"
                                    )
                                }
                            )

                            CustomDisabledTextField(
                                modifier = Modifier.weight(2f),
                                value = "${event?.dtStart?.hour} : ${event?.dtStart?.minute}",
                                onClick = {
                                    //TODO open Alarm?
                                },
                                label = stringResource(id = R.string.eventStartTimeLabel),
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "c-d"
                                    )
                                }
                            )
                        }


                        //TODO maybe keep end date same as start date now and only change time

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            CustomDisabledTextField(
                                modifier = Modifier.weight(3f),
                                value = "${event?.dtEnd?.dayOfMonth}/${event?.dtEnd?.monthValue}/${event?.dtEnd?.year}",
                                onClick = {
                                    //Todo open calendar?
                                },
                                label = stringResource(id = R.string.eventEndDateLabel),
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = "c-d"
                                    )
                                }
                            )

                            CustomDisabledTextField(
                                modifier = Modifier.weight(2f),
                                value = "${event?.dtEnd?.hour} : ${event?.dtEnd?.minute}",
                                onClick = {
                                    //TODO open Alarm?
                                },
                                label = stringResource(id = R.string.eventEndTimeLabel),
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "c-d"
                                    )
                                }
                            )
                        }


                        CustomDisabledTextField(
                            value = event?.location.toString(),
                            onClick = {
                                startImplicitIntent(
                                    context, Uri.parse(
                                        "geo:0,0?q=${
                                            URLEncoder.encode(
                                                event?.location ?: "420 High Street".ifEmpty {
                                                    "420 High Street"
                                                },
                                                "UTF-8"
                                            )
                                        }"
                                    )
                                )
                            },
                            label = stringResource(id = R.string.eventLocationLabel),
                            trailingIcon = { }
                        )

                        CustomDisabledTextField(
                            value = event?.geo.toString(),
                            onClick = {
                                startImplicitIntent(
                                    context,
                                    Uri.parse(
                                        "geo:${event?.geo}"
                                    )
                                )
                            },
                            label = stringResource(id = R.string.eventGeoTagLabel),
                            trailingIcon = { }
                        )

                        CustomDisabledTextField(
                            value = event?.url.toString(),
                            onClick = {
                                //TODO open browser
                            },
                            label = stringResource(id = R.string.eventUrlLabel),
                            trailingIcon = { }
                        )
                    }

                    Column {
                        Divider()
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.End,
                        ) {
                            Button(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp),
                                onClick = {
                                    closeDialog()
                                }) {
                                Text(text = stringResource(id = R.string.closeText))
                            }
                            //                        }
                        }

                    }
                }
            }
        }
    }
}




