package com.nsi.ezcalender.ui

import androidx.compose.runtime.Composable
import com.nsi.ezcalender.ui.screens.HomeScreen

@Composable
fun EzCalenderApp(mainVewModel: MainViewModel) {

    HomeScreen(mainVewModel)
//    NavHost()
}