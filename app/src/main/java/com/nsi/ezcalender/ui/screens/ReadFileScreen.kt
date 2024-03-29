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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.nsi.ezcalender.MainViewModel
import com.nsi.ezcalender.R
import com.nsi.ezcalender.model.Event
import com.nsi.ezcalender.model.SortOptions
import com.nsi.ezcalender.ui.common.LoadingAlertDialog
import com.nsi.ezcalender.ui.common.ViewEventDialog
import com.nsi.ezcalender.ui.common.getResourceByName

@Composable
fun ReadFileScreen(
    mainViewModel: MainViewModel,
    navigateUp: () -> Unit
) {
    val state = mainViewModel.state.value
    var openViewEventDialog by rememberSaveable { mutableStateOf(false) }

    if (state.isLoading) {
        LoadingAlertDialog(
            stringResource(id = R.string.loadingTitle),
            stringResource(id = R.string.loadingDescription),
            false
        ) {}
    } else if (state.error != null) {
        LoadingAlertDialog(
            stringResource(id = R.string.errorTitle),
            getResourceByName(state.error.message),
            true
        ) {
            mainViewModel.onErrorCleared()
        }
    }


    ReadFileScreenContent(
        openViewEventDialog = openViewEventDialog,
        events = state.eventsList,
        sortByDate = {
            mainViewModel.sort(SortOptions.DATE_START)
        },
        sortBySummary = {
            mainViewModel.sort(SortOptions.SUMMARY_NAME)
        },
        sortByLocationName = {
            mainViewModel.sort(SortOptions.LOCATION_NAME)
        },
        updateViewEventDialog = {
            openViewEventDialog = it
        }
    )

    BackHandler {
        navigateUp()
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReadFileScreenContent(
    openViewEventDialog: Boolean,
    events: List<Event>,
    sortByDate: () -> Unit,
    sortBySummary: () -> Unit,
    sortByLocationName: () -> Unit,
    updateViewEventDialog: (Boolean) -> Unit,

    ) {
    val context = LocalContext.current
    var selectedEvent: Int? by rememberSaveable { mutableStateOf(if (events.isEmpty()) null else 0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
//            .padding(bottom = 60.dp)
    ) {

        if (events.isNotEmpty()) {
            SortOptions(
                sortByDate = sortByDate,
                sortBySummary = sortBySummary,
                sortByLocationName = sortByLocationName
            )
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
                    text = stringResource(id = R.string.readEventsNoContentDescription),
                    textAlign = TextAlign.Center
                )

            }
        }
        if (openViewEventDialog) {
            ViewEventDialog(
                event = events[selectedEvent!!],
                openDialog = openViewEventDialog,
                closeDialog = {
                    updateViewEventDialog(false)
                }
            )
        }

        LazyColumn {
            itemsIndexed(items = events, key = { _, event -> event.uid!! }
            ) { index, event ->
                EventComposable(
                    modifier = Modifier.animateItemPlacement(),
                    context,
                    event,
                    onClick = {
                        selectedEvent = index //it
                        updateViewEventDialog(true)
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
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(stringResource(id = R.string.sort))
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

