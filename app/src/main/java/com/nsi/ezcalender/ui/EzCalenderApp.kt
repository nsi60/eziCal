package com.nsi.ezcalender.ui

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.nsi.ezcalender.ui.navigation.BottomNavigationThing
import com.nsi.ezcalender.ui.navigation.NavigationHost
import com.nsi.ezcalender.ui.screens.HomeScreen

@Composable
fun EzCalenderApp() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationThing(navController = navController) }
    ) {
//        BottomNavigationThing(navController = navController)
        NavigationHost(navController)
    }
}