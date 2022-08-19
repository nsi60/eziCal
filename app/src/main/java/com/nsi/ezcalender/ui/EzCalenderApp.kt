package com.nsi.ezcalender.ui

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nsi.ezcalender.ui.navigation.BottomNavigationThing
import com.nsi.ezcalender.ui.navigation.NavigationHost
import com.nsi.ezcalender.ui.screens.Screens

@Composable
fun EzCalenderApp() {
    val navController = rememberNavController()
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    when (navBackStackEntry?.destination?.route) {
        Screens.SplashScreen.route -> bottomBarState.value = false
        else -> bottomBarState.value = true

    }

    Scaffold(
        bottomBar = {
            if (bottomBarState.value) {
                BottomNavigationThing(navController = navController)
            }

        },
    ) {
//        BottomNavigationThing(navController = navController)
        NavigationHost(navController)
    }
}
