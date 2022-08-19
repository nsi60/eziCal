package com.nsi.ezcalender.ui.common

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.animation.*
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.nsi.ezcalender.model.Event
import com.nsi.ezcalender.ui.screens.CustomDisabledTextField
import com.nsi.ezcalender.ui.screens.CustomTextField
import java.time.LocalDateTime
import java.util.*

@Composable
fun CustomAlertDialog() {
    AlertDialog(
        onDismissRequest = {
//            openDialog.value = false
        },
        title = {
            Text(text = "Loading")
        },
        text = {
            Text(
                "Loading stuff"
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
                    Text("Loading")
                }
            }
        }
    )

}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FullScreenAlertDialog(
    openDialog: Boolean,
    closeDialog: () -> Unit,
    saveEvent: (Event, Boolean) -> Unit

) {
    val context = LocalContext.current
    var createAnotherEventCheck by remember { mutableStateOf(true) }


    var summaryText by remember { mutableStateOf(TextFieldValue("")) }

    var dateStartObject by remember { mutableStateOf(LocalDateTime.now()) }
    var dateEndObject by remember { mutableStateOf(dateStartObject.plusHours(1)) }

    var locationText by remember { mutableStateOf(TextFieldValue("")) }
    var geoText by remember { mutableStateOf(TextFieldValue("")) }
    var linkText by remember { mutableStateOf(TextFieldValue("")) }

    val calendar by remember { mutableStateOf(Calendar.getInstance()) }


    var timeStartText by remember { mutableStateOf("${dateStartObject.hour} : ${dateStartObject.minute}") }
    var timeEndText by remember { mutableStateOf("${dateEndObject.hour} : ${dateEndObject.minute}") }

    var dateStartText by remember { mutableStateOf("${dateStartObject.dayOfMonth}/${dateStartObject.monthValue}/${dateStartObject.year}") }
//    var dateEndText by remember { mutableStateOf("") }
    var dateEndText by remember { mutableStateOf(dateStartText) }  //as long as end date is disabled

    val startTimePickerDialog = remember {
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

            },
            calendar.get(Calendar.HOUR),
            calendar.get(Calendar.MINUTE),
            false
        )
    }

    val endTimePickerDialog = remember {
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
            },
            calendar.get(Calendar.HOUR),
            calendar.get(Calendar.MINUTE),
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

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
        )
    }


    val endDatePickerDialog = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                dateEndText = "$dayOfMonth/${month + 1}/$year"
//                endTimePickerDialog.show()
                dateEndObject = LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
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
                            Text(text = "title")
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
                            label = "Summary",
                            placeHolder = "Christmas Day"
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {

                            CustomDisabledTextField(
                                modifier = Modifier.weight(3f),
                                value = dateStartText,
                                startDatePickerDialog = {
                                    startDatePickerDialog.show()
                                },
                                label = "Start date",
                                placeHolder = "25 December",
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
                                startDatePickerDialog = {
                                    startTimePickerDialog.show()
                                },
                                label = "Start time",
                                placeHolder = "12:00",
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
                                startDatePickerDialog = {
//                                endDatePickerDialog.show() //Todo have disabled it for now, handling same day events
                                },
                                label = "End date",
                                placeHolder = "30 December",
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
                                startDatePickerDialog = {
                                    endTimePickerDialog.show()
                                },
                                label = "End time",
                                placeHolder = "23:59",
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
                            label = "Location",
                            placeHolder = "420 High Street"
                        )

                        CustomTextField(
                            text = geoText,
                            updateText = { geoText = it },
                            label = "Geo Tags",
                            placeHolder = "4,20"
                        )

                        CustomTextField(
                            text = linkText,
                            updateText = { linkText = it },
                            label = "URL",
                            placeHolder = "link.com"
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
                                Text("Create new event")
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
                                    Text(text = "Cancel")
                                }

                                Button(modifier = Modifier
                                    .padding(horizontal = 4.dp),
                                    onClick = {
                                        val event = Event(
                                            summary = summaryText.text.ifEmpty { null },
                                            dtStart = dateStartObject, //dateStartText.text,
                                            dtEnd = dateEndObject, //dateEndText.text,
                                            location = locationText.text.ifEmpty { "" },
                                            geo = geoText.text.ifEmpty { "0,0" }
                                        )
                                        saveEvent(event, createAnotherEventCheck)
                                    }) {
                                    Text(text = "Save")
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}





