package ru.jpscissor.frprototype.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.jpscissor.frprototype.screens.HomeScreen

sealed class NavRoute (val route: String) {
    object Home: NavRoute("onboard_screen")

}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = NavRoute.Home.route) {

        mainGraph(navController)

    }

}

fun NavGraphBuilder.mainGraph(navController: NavController) {

    composable(NavRoute.Home.route) {
        HomeScreen()
    }

}