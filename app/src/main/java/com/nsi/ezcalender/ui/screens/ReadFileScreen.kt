package com.nsi.ezcalender.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.nsi.ezcalender.model.Event
import com.nsi.ezcalender.model.SortOptions
import com.nsi.ezcalender.ui.MainViewModel
import com.nsi.ezcalender.ui.common.CustomAlertDialog
import java.net.URLEncoder

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

    Column(modifier = Modifier.fillMaxSize()) {

        if (events.isNotEmpty()) {
            SortOptions(
                sortByDate = sortByDate,
                sortBySummary = sortBySummary,
                sortByLocationName = sortByLocationName
            )
        } else {
            Row(
                Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Nothing to show.")
            }
        }

        LazyColumn() {
            items(events, key = { it.uid!! }) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .shadow(elevation = 2.dp)
                        .animateItemPlacement()   //TODO not the best looking
                        .clickable {
                            startImplicitIntent(
                                context, Uri.parse(
                                    "geo:${it.geo}?q=${URLEncoder.encode(it.location, "UTF-8")}"
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