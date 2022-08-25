package com.nsi.ezcalender.ui.navigation

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.nsi.ezcalender.ui.common.getResourceByName
import com.nsi.ezcalender.ui.screens.Screens

@Composable
fun BottomNavigationThing(
    navController: NavHostController
) {

    val items = listOf(
        Screens.HomeScreen,
        Screens.ReadFileScreen,
        Screens.CreateFileScreen,
    )

    BottomNavigation(
        backgroundColor = MaterialTheme.colors.secondaryVariant,
        contentColor = contentColorFor(MaterialTheme.colors.secondaryVariant)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                label = { Text(text = getResourceByName(item.route)) },
                selected = currentRoute == item.route,
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Black.copy(0.4f),
                onClick = {
                    navController.navigate(item.route) {

                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(imageVector = item.icon, contentDescription = "c-d") }
            )
        }

    }
}





