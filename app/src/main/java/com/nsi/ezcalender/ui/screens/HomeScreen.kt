package com.nsi.ezcalender.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.nsi.ezcalender.ui.MainViewModel
import net.fortuna.ical4j.model.Component

@Composable
fun HomeScreen(mainVewModel: MainViewModel) {

    val state = mainVewModel.state

    Column {
        Button(onClick = {
//            mainVewModel.readIcs()
//            mainVewModel.readIcs_2()
            mainVewModel.readIcs_3()

        }) {
            Text(text = "Read ICS")
        }


        state.value.eventsList.forEach {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                Text(text = it.summary)
            }
        }
    }


}


