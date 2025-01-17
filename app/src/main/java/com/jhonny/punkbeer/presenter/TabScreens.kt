package com.jhonny.punkbeer.presenter

import android.os.Parcelable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jhonny.punkbeer.presenter.detail.DetailScreen
import com.jhonny.punkbeer.presenter.entities.CocktailPresentation
import com.jhonny.punkbeer.presenter.favorites.FavoritesScreen
import com.jhonny.punkbeer.presenter.main.MainScreen
import com.jhonny.punkbeer.presenter.navigation.AppScreens
import com.jhonny.punkbeer.util.parcelable

@Composable
fun TabbedScreen(navController: NavHostController) {
    val navController = rememberNavController()
    val screens = listOf(
        AppScreens.MainScreen,
        AppScreens.FavoritesScreen
    )
    val currentRoute by rememberUpdatedState(navController.currentBackStackEntry?.destination?.route)

    Scaffold(
        topBar = {
            TabRow(
                selectedTabIndex = screens.indexOfFirst { screen ->
                    screen.route == currentRoute
                }.coerceAtLeast(0),
            ) {
                screens.forEachIndexed { index, screen ->
                    Tab(
                        text = { Text(screen.name) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreens.MainScreen.route,
            Modifier.padding(innerPadding)
        ) {
            composable(AppScreens.MainScreen.route) {
                MainScreen(navController)
            }
            composable(AppScreens.FavoritesScreen.route) {
                FavoritesScreen(navController)
            }
            composable(route = AppScreens.DetailScreen.route + "/{beer}", arguments =
            listOf(navArgument(name = "beer") {
                type = NavType.StringType

            })
            ) {
                val beer: Parcelable? =  it.arguments?.parcelable("beer")
                val result =
                    navController.previousBackStackEntry?.savedStateHandle?.get<CocktailPresentation?>("beer")
                DetailScreen(navController, result?: CocktailPresentation())

            }
        }
    }
}