package com.nsi.ezcalender.ui.screens

sealed class Screens(val route: String) {
    object HomeScreen: Screens("homeScreen")
    object ReadFileScreen: Screens("readFileScreen")
    object CreateFileScreen: Screens("createFileScreen")

}
