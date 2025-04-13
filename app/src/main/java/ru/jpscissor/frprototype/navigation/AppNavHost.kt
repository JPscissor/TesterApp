package ru.jpscissor.frprototype.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.jpscissor.frprototype.data.getQuestions
import ru.jpscissor.frprototype.screens.HomeScreen
import ru.jpscissor.frprototype.screens.TestScreen

sealed class NavRoute (val route: String) {
    object Home: NavRoute("home_screen")
    object Test: NavRoute("test_screen")

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
        HomeScreen( onNavigateToTest = { navController.navigate(NavRoute.Test.route) } )
    }

    composable(NavRoute.Test.route) {
        TestScreen( onBack = {}, getQuestions())
    }

}