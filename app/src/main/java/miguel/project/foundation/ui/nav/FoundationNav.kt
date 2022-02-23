package miguel.project.foundation.ui.nav

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import miguel.project.foundation.ui.nav.MainDestinations.DETAIL_ROUTE
import miguel.project.foundation.ui.nav.MainDestinations.MAIN_ROUTE
import miguel.project.foundation.ui.screen.DetailScreen
import miguel.project.foundation.ui.screen.MainScreen
import org.koin.androidx.compose.get


object MainDestinations {
    const val MAIN_ROUTE = "main"
    const val DETAIL_ROUTE = "detail"
}

@Composable
fun FoundationNavGraph(
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    startDestination: String = MAIN_ROUTE
) {
    val actions = remember(navController) { MainActions(navController) }
    NavHost(
        navController,
        startDestination = startDestination
    ) {
        composable(route = MAIN_ROUTE) {
            MainScreen(
                get(),
                get(),
                actions.navigateToDetails
            )
        }

        composable(
            route = "$DETAIL_ROUTE/{index}",
            arguments = listOf(navArgument("index") { type = NavType.IntType })
        ){ backStackEntry ->
            val index = backStackEntry.arguments?.getInt("index") ?: 0
            DetailScreen(get(), get(), index)
        }
    }
}

class MainActions(navController: NavHostController) {
    val navigateToDetails: (index:Int) -> Unit = {
        navController.navigate("$DETAIL_ROUTE/$it")
    }
}
