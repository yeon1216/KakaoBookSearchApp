package com.example.searchbookapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.searchbookapp.ui.components.SearchBookSnackbar
import com.example.searchbookapp.ui.theme.SearchBookTheme

@Composable
fun SearchBookApp() {

    SearchBookTheme {

        val appState = rememberSearchBookAppState()

        Scaffold(
            snackbarHost = {
                SnackbarHost(
                    hostState = it,
                    modifier = Modifier.systemBarsPadding(),
                    snackbar = { snackbarData -> SearchBookSnackbar(snackbarData) }
                )
            },
            scaffoldState = appState.scaffoldState
        ) { innerPaddingModifier ->
            SearchBookNavigation(
                appState = appState,
                modifier = Modifier.padding(innerPaddingModifier)
            )
        }

    }

}