package com.nsi.ezcalender.ui.screens

import EventComposable
import EzIcalLogoGif
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.nsi.ezcalender.R
import com.nsi.ezcalender.model.Event
import com.nsi.ezcalender.model.SortOptions
import com.nsi.ezcalender.ui.MainViewModel
import com.nsi.ezcalender.ui.common.CustomAlertDialog
import com.nsi.ezcalender.ui.common.ViewEventDialog

@Composable
fun ReadFileScreen(
    mainViewModel: MainViewModel,
    navigateUp: () -> Unit
) {
    val state = mainViewModel.state.value

    if (state.isLoading) {
        CustomAlertDialog()
    }


    ReadFileScreenContent(
        events = state.eventsList,
        sortByDate = {
            mainViewModel.sort(SortOptions.DATE_START)
        },
        sortBySummary = {
            mainViewModel.sort(SortOptions.SUMMARY_NAME)
        },
        sortByLocationName = {
            mainViewModel.sort(SortOptions.LOCATION_NAME)
        }
    )

    BackHandler {
        navigateUp()
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReadFileScreenContent(
    events: List<Event>,
    sortByDate: () -> Unit,
    sortBySummary: () -> Unit,
    sortByLocationName: () -> Unit

) {
    val context = LocalContext.current
    var openDialog by remember { mutableStateOf(false) }
    var selectedEvent: Event? by remember { mutableStateOf(null) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 60.dp)
    ) {

        if (events.isNotEmpty()) {
            SortOptions(
                sortByDate = sortByDate,
                sortBySummary = sortBySummary,
                sortByLocationName = sortByLocationName
            )
        } else {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                EzIcalLogoGif(Modifier.scale(0.5f))
                Text(
                    stringResource(id = R.string.readEventsNoContentDescription),
                    textAlign = TextAlign.Center
                )

            }
        }

        ViewEventDialog(
            event = selectedEvent,
            openDialog = openDialog,
            closeDialog = { openDialog = false }
        )

        LazyColumn {
            items(events, key = { it.uid!! }) { event ->
                EventComposable(
                    modifier = Modifier.animateItemPlacement(),
                    context,
                    event,
                    onClick = {
                        selectedEvent = it
                        openDialog = true
                    }
                )
            }
        }
    }

}

@Composable
fun SortOptions(
    sortByDate: () -> Unit,
    sortBySummary: () -> Unit,
    sortByLocationName: () -> Unit
) {

    Row(
        Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        IconButton(onClick = {
            sortByDate()
        }) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "content-description"
            )
        }
        IconButton(onClick = {
            sortBySummary()
        }) {
            Icon(imageVector = Icons.Default.Info, contentDescription = "content-description")
        }
        IconButton(onClick = {
            sortByLocationName()
        }) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "content-description"
            )
        }

    }

}

fun startImplicitIntent(context: Context, uri: Uri) {
    val intent = Intent(
        Intent.ACTION_VIEW,
        uri
    )
    startActivity(context, intent, null)
}

