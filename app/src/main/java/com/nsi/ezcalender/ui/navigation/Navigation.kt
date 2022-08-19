package com.nsi.ezcalender.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nsi.ezcalender.ui.MainViewModel
import com.nsi.ezcalender.ui.screens.*

@Composable
fun NavigationHost(
    navController: NavHostController
) {
    val mainViewModel = hiltViewModel<MainViewModel>()


    fun navigateToHomeScreen() {
        navController.navigate(Screens.HomeScreen.route)
    }


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
        startDestination = Screens.SplashScreen.route
    ) {

        composable(Screens.SplashScreen.route) {
            SplashScreen(
                mainVewModel = mainViewModel,
                navigateToHomeScreen = {
                    navController.popBackStack()
                    navigateToHomeScreen()
                }
            )
        }


        composable(Screens.HomeScreen.route) {
            HomeScreen(
                mainVewModel = mainViewModel,
                navigateToReadFileScreen = { navigateToReadFileScreen() },
                navigateToCreateFileScreen = { navigateToCreateFileScreen() }
            )
        }

        composable(Screens.ReadFileScreen.route) {
            ReadFileScreen(
                mainViewModel = mainViewModel,
                navigateUp = { navigationPopUp() },
            )
        }

        composable(Screens.CreateFileScreen.route) {
            CreateFileScreen(
                mainViewModel = mainViewModel,
                navigateUp = { navigationPopUp() },
            )
        }
    }

}