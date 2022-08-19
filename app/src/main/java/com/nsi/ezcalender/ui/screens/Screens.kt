package com.nsi.ezcalender.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screens(val route: String, val icon: ImageVector) {
    object SplashScreen : Screens("splashScreen", Icons.Default.DateRange)
    object HomeScreen : Screens("homeScreen", Icons.Default.Home)
    object ReadFileScreen : Screens("readFileScreen", Icons.Default.List)
    object CreateFileScreen : Screens("createFileScreen", Icons.Default.Add)

}
