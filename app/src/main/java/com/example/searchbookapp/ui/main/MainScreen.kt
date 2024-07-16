package com.example.searchbookapp.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.searchbookapp.R
import kotlinx.coroutines.flow.collectLatest
import com.example.searchbookapp.ui.theme.Black
import com.example.searchbookapp.ui.theme.BookTextStyles
import com.example.searchbookapp.ui.main.MainContract.*
import com.example.searchbookapp.ui.theme.Gray700
import com.example.searchbookapp.ui.theme.Gray900
import com.example.searchbookapp.ui.theme.Purple500
import com.example.searchbookapp.ui.theme.Red500
import com.example.searchbookapp.ui.theme.White

@Composable
fun MainScreen(
    navigateToSearchBook: () -> Unit,
    navigateToFavorite: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()

    Main(
        viewState = viewState,
        onClickSearchBookBtn = {
            viewModel.setEvent(MainEvent.OnClickSearchBookBtn)
        },
        onClickFavoriteBookBtn = {
            viewModel.setEvent(MainEvent.OnClickFavoriteBookBtn)
        },
        modifier = Modifier
    )

    LaunchedEffect(key1 = viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is MainSideEffect.NavigateToSearchBook -> {
                    navigateToSearchBook()
                }
                is MainSideEffect.NavigateToFavorite -> {
                    navigateToFavorite()
                }
            }
        }
    }

    LaunchedEffect(key1 = true) {

    }
}

@Composable
private fun Main(
    viewState: MainViewState,
    onClickSearchBookBtn: () -> Unit,
    onClickFavoriteBookBtn: () -> Unit,
    modifier: Modifier
) {



    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
        ) {
            Spacer(Modifier.height(60.dp))
            Text(
                text = stringResource(id = R.string.app_name),
                style = BookTextStyles.header5Bold(Purple500)
            )
            Spacer(Modifier.height(111.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_main_characters),
                contentDescription = null,
                modifier = Modifier.size(width = 189.dp, height = 173.dp)
            )
        }


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Box(
                modifier = Modifier
                    .height(48.dp)
                    .padding(start = 16.dp, end = 16.dp)
                    .background(
                        color = Gray900,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        onClickSearchBookBtn()
                    },
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "search_icon",
                        tint = White,
                        modifier = modifier
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = R.string.search_book),
                        style = BookTextStyles.buttonBold(White)
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .height(48.dp)
                    .padding(start = 16.dp, end = 16.dp)
                    .background(
                        color = Gray900,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        onClickFavoriteBookBtn()
                    },
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Favorite,
                        contentDescription = "favorite_icon",
                        tint = Red500,
                        modifier = modifier
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = R.string.favorite_book),
                        style = BookTextStyles.buttonBold(White)
                    )
                }
            }
            Spacer(modifier = Modifier.height(70.dp))
        }


    }
}
