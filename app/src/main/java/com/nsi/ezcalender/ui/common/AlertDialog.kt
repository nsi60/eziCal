package com.nsi.ezcalender.ui.common

import android.app.AppComponentFactory
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.android.material.datepicker.MaterialDatePicker
import com.nsi.ezcalender.MainActivity
import com.nsi.ezcalender.model.Event
import com.nsi.ezcalender.ui.screens.CustomTextField
import java.time.LocalDate
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
    closeDialog: () -> Unit,
    saveEvent: (Event) -> Unit

) {
    val context = LocalContext.current

    var summaryText by remember { mutableStateOf(TextFieldValue("")) }

    var dateStartObject by remember { mutableStateOf(LocalDateTime.now()) }
    var dateEndObject by remember { mutableStateOf(LocalDateTime.now()) }

    var locationText by remember { mutableStateOf(TextFieldValue("")) }
    var geoText by remember { mutableStateOf(TextFieldValue("")) }
    var linkText by remember { mutableStateOf(TextFieldValue("")) }

    val calendar by remember { mutableStateOf(Calendar.getInstance()) }


    var timeStartText by remember { mutableStateOf("") }
    var timeEndText by remember { mutableStateOf("") }

    var dateStartText by remember { mutableStateOf("") }
    var dateEndText by remember { mutableStateOf("") }

    val startTimePickerDialog = remember {
        TimePickerDialog(
            context,
            { _: TimePicker, hour: Int, minute: Int ->
                timeStartText = "$hour : $minute"
                dateStartText = "$dateStartText $timeStartText"
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
                dateEndText = "$dateEndText $timeEndText"
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
                dateStartText = "$dayOfMonth/$month/$year"
                startTimePickerDialog.show()
                dateStartObject = LocalDateTime.of(year, month, dayOfMonth, 0, 0)

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
        )
    }


    val endDatePickerDialog = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, year   : Int, month: Int, dayOfMonth: Int ->
                dateEndText = "$dayOfMonth/$month/$year"
                endTimePickerDialog.show()
                dateEndObject = LocalDateTime.of(year, month, dayOfMonth, 0, 0)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }





    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = {}
    ) {

        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(verticalArrangement = Arrangement.SpaceBetween) {
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
                            Icon(imageVector = Icons.Default.Close, contentDescription = "c-d")
                        }
                        Text(text = "title")
                    }
                    Divider()
                }

                Column {
                    CustomTextField(
                        text = summaryText,
                        updateText = { summaryText = it },
                        label = "Summary",
                        placeHolder = "Christmas Day"
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(
                                color = Color.Unspecified,
                                shape = CircleShape,
                            )
                            .clickable {
                                startDatePickerDialog.show()
                            },
                        value = dateStartText,
                        onValueChange = {},
                        label = { Text("Start date") },
                        placeholder = { Text("25 December") },
//                        readOnly = true,
                        enabled = false,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            disabledTextColor = LocalContentColor.current.copy(LocalContentAlpha.current)
                        )
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(
                                color = Color.Unspecified,
                                shape = CircleShape,
                            )
                            .clickable {
                                endDatePickerDialog.show()
                            },
                        value = dateEndText,
                        onValueChange = {},
                        label = { Text("End date") },
                        placeholder = { Text("30 December") },
//                        readOnly = true,
                        enabled = false,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            disabledTextColor = LocalContentColor.current.copy(LocalContentAlpha.current)
                        )
                    )


//                    CustomTextField(
//                        text = dateStartText,
//                        updateText = { dateStartText = it },
//                        label = "Start date",
//                        placeHolder = "25 December"
//                    )
//
//                    CustomTextField(
//                        text = dateEndText,
//                        updateText = { dateEndText = it },
//                        label = "End date",
//                        placeHolder = "30 December"
//                    )


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
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(modifier = Modifier
//                        .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                            onClick = {
                                closeDialog()
                            }) {
                            Text(text = "Cancel")
                        }

                        Button(modifier = Modifier
//                        .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                            onClick = {
                                val event = Event(
                                    summary = summaryText.text,
                                    dtStart = dateStartObject, //dateStartText.text,
                                    dtEnd = dateEndObject, //dateEndText.text,
                                    location = locationText.text,
                                    geo = geoText.text
                                )
                                saveEvent(event)
                            }) {
                            Text(text = "Save")
                        }
                    }
                }
            }
        }
    }
}




