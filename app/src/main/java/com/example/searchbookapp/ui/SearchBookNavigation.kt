package com.example.searchbookapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.searchbookapp.ui.book.BookScreen
import com.example.searchbookapp.ui.favofite.FavoriteScreen
import com.example.searchbookapp.ui.main.MainScreen

@Composable
fun SearchBookNavigation(
    appState: SearchBookAppState,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = appState.navController,
        startDestination = SearchBookDestinations.MAIN_ROUTE,
        modifier = modifier
    ) {

        composable(
            route = SearchBookDestinations.MAIN_ROUTE
        ) { backStackEntry ->
            MainScreen(
                navigateToSearchBook = { appState.navigateToSearchBook(backStackEntry) },
                navigateToFavorite = { appState.navigateToFavorite(backStackEntry) }
            )
        }
        composable(
            route = SearchBookDestinations.BOOK_ROUTE
        ) { backStackEntry ->
            BookScreen(
                appState = appState
            )
        }
        composable(
            route = SearchBookDestinations.FAVORITE_ROUTE
        ) {
            FavoriteScreen(
                appState = appState
            )
        }
    }

}