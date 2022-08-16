package com.nsi.ezcalender.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nsi.ezcalender.ui.MainViewModel
import com.nsi.ezcalender.ui.screens.CreateFileScreen
import com.nsi.ezcalender.ui.screens.Screens
import com.nsi.ezcalender.ui.screens.HomeScreen
import com.nsi.ezcalender.ui.screens.ReadFileScreen

@Composable
fun NavigationHost(
    navController: NavHostController = rememberNavController(),
    mainViewModel: MainViewModel
) {

    fun navigateToReadFileScreen() {
        navController.navigate(Screens.ReadFileScreen.route)
    }

    fun navigateToCreateFileScreen() {
        navController.navigate(Screens.CreateFileScreen.route)
    }

    fun navigationPopUp() {
        navController.popBackStack()
    }

    NavHost(
        navController = navController,
        startDestination = Screens.HomeScreen.route
    ) {
        composable(Screens.HomeScreen.route) {
            HomeScreen(
                mainVewModel =  mainViewModel,
                navigateToReadFileScreen = { navigateToReadFileScreen() },
                navigateToCreateFileScreen = { navigateToCreateFileScreen()  }
            )
        }

        composable(Screens.ReadFileScreen.route) {
            ReadFileScreen(
                mainViewModel =  mainViewModel,
                navigateUp = { navigationPopUp() },
            )
        }

        composable(Screens.CreateFileScreen.route) {
            CreateFileScreen(
                mainViewModel =  mainViewModel,
                navigateUp = { navigationPopUp() },
            )
        }
    }

}