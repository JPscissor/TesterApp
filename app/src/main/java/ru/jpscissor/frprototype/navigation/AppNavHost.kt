package ru.jpscissor.frprototype.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.jpscissor.frprototype.R
import ru.jpscissor.frprototype.data.parseTestFromJson
import ru.jpscissor.frprototype.screens.HomeScreen
import ru.jpscissor.frprototype.screens.SampleScreen
import ru.jpscissor.frprototype.screens.TestScreen

sealed class NavRoute (val route: String) {
    object Home: NavRoute("home_screen")
    object Test: NavRoute("test_screen")
    object Settings: NavRoute("sample_screen")

}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(navController, startDestination = NavRoute.Home.route) {

        mainGraph(navController, context)

    }

}

fun NavGraphBuilder.mainGraph(navController: NavController, context: Context) {

    composable(NavRoute.Home.route) {
        HomeScreen( onNavigateToTest = { navController.navigate(NavRoute.Test.route) } )
    }

    composable(NavRoute.Test.route) {
        TestScreen( onBack = {}, parseTestFromJson(context, R.raw.psyhology).questions)
    }

}