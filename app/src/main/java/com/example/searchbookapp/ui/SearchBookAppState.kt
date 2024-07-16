package com.example.searchbookapp.ui

import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.searchbookapp.data.model.Book
import com.example.searchbookapp.ui.utils.SnackbarManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object SearchBookDestinations {

    const val MAIN_ROUTE = "main"
    const val BOOK_ROUTE = "main/book"
    const val FAVORITE_ROUTE = "main/favorite"

}

@Composable
fun rememberSearchBookAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember(scaffoldState, navController, snackbarManager, resources, coroutineScope) {
    SearchBookAppState(scaffoldState, navController, snackbarManager, resources, coroutineScope)
}

@Stable
class SearchBookAppState(
    val scaffoldState: ScaffoldState,
    val navController: NavHostController,
    private val snackbarManager: SnackbarManager,
    private val resources: Resources,
    coroutineScope: CoroutineScope
) {
    init {
        coroutineScope.launch {
            snackbarManager.messages.collect { currentMessages ->
                if (currentMessages.isNotEmpty()) {
                    val message = currentMessages[0]
                    val text = resources.getText(message.messageId)
                    scaffoldState.snackbarHostState.showSnackbar(text.toString())
                    snackbarManager.setMessageShown(message.id)
                }
            }
        }
    }

    fun showSnackbarMessage(@StringRes messageId: Int) {
        snackbarManager.showMessage(messageId)
    }

    val currentRoute: String?
        get() = navController.currentDestination?.route

    fun upPress() {
        navController.navigateUp()
    }
    fun navigateToSearchBook(from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(SearchBookDestinations.BOOK_ROUTE)
        }
    }
    fun navigateToFavorite(from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(SearchBookDestinations.FAVORITE_ROUTE)
        }
    }

}

private fun NavBackStackEntry.lifecycleIsResumed() = this.lifecycle.currentState == Lifecycle.State.RESUMED

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}

/**
 * A composable function that returns the [Resources]. It will be recomposed when `Configuration`
 * gets updated.
 */
@Composable
@ReadOnlyComposable
private fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}
