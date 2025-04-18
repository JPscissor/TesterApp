package ru.jpscissor.frprototype.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.jpscissor.frprototype.R
import ru.jpscissor.frprototype.data.loadTestFromJson
import ru.jpscissor.frprototype.data.parseTestFromJson
import ru.jpscissor.frprototype.screens.HomeScreen
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
        HomeScreen(
            context = LocalContext.current,
            onNavigateToTest = { test ->
                navController.navigate("${NavRoute.Test.route}/${test.hashCode()}")
            }
        )
    }
    composable(
        "${NavRoute.Test.route}/{testId}",
        arguments = listOf(navArgument("testId") { type = NavType.IntType })
    ) { backStackEntry ->
        val testId = backStackEntry.arguments?.getInt("testId") ?: return@composable
        val test = loadTestFromJson(context, testId)
        TestScreen(
            onBack = { navController.popBackStack() },
            test = test,
            context = context
        )
    }
}