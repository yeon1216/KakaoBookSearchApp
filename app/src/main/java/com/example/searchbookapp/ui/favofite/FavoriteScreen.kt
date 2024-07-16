package com.example.searchbookapp.ui.favofite

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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.RangeSlider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.searchbookapp.R
import com.example.searchbookapp.data.model.Book
import com.example.searchbookapp.ui.SearchBookAppState
import com.example.searchbookapp.ui.book.BookListView
import kotlinx.coroutines.flow.collectLatest
import com.example.searchbookapp.ui.favofite.FavoriteContract.*
import com.example.searchbookapp.ui.theme.Black
import com.example.searchbookapp.ui.theme.BookTextStyles
import com.example.searchbookapp.ui.theme.Gray700
import kotlinx.coroutines.launch

@Composable
fun FavoriteScreen(
    appState: SearchBookAppState,
    viewModel: FavoriteViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()
    val scope = rememberCoroutineScope()

    Favorite(
        viewState = viewState,
        onClickBackIcon = { viewModel.setEvent(FavoriteEvent.OnClickBackIcon) },
        onClickFavoriteIcon = { book -> viewModel.setEvent(FavoriteEvent.OnClickFavoriteIcon(book)) },
        onClickBookItem = {},
        onClickSortIcon = { viewModel.setEvent(FavoriteEvent.OnClickSortIcon) },
        updateMinMaxPrice = { min, max -> viewModel.setEvent(FavoriteEvent.UpdateMinMaxPrice(min, max)) },
        onPriceSliderValueChangeFinished = { viewModel.setEvent(FavoriteEvent.OnPriceSliderValueChangeFinished) },
        modifier = Modifier
    )

    LaunchedEffect(key1 = viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is FavoriteSideEffect.NavigateUp -> {
                    appState.upPress()
                }
                is FavoriteSideEffect.ShowSnackBar -> {
                    scope.launch {
                        appState.showSnackbarMessage(effect.resId)
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.setEvent(FavoriteEvent.InitScreen)
    }
}

@Composable
private fun Favorite(
    viewState: FavoriteViewState,
    onClickBackIcon: () -> Unit,
    onClickBookItem: (Book) -> Unit,
    onClickFavoriteIcon: (Book) -> Unit,
    onClickSortIcon: () -> Unit,
    updateMinMaxPrice: (Int, Int) -> Unit,
    onPriceSliderValueChangeFinished: () -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column {
            FavoriteTopBar(
                onClickBackIcon,
                onClickSortIcon,
                modifier = modifier
            )
            FavoriteSortBar(
                updateMinMaxPrice,
                onPriceSliderValueChangeFinished,
                modifier = modifier
            )
            Box(
                modifier = modifier.fillMaxSize()
            ) {

                Box(
                    modifier = modifier.fillMaxSize()
                ) {
                    BookListView(
                        bookList = viewState.bookList,
                        onClickBookItem = { book ->
                            onClickBookItem(book)
                        },
                        onClickFavoriteIcon = { book ->
                            onClickFavoriteIcon(book)
                        },
                        modifier = modifier
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FavoriteSortBar(
    updateMinMaxPrice: (Int, Int) -> Unit,
    onPriceSliderValueChangeFinished: () -> Unit,
    modifier: Modifier
) {
    var sliderValues by remember { mutableStateOf(1000f..100000f) }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "최소: ${sliderValues.start.toInt()}원", modifier = Modifier.padding(8.dp))
                Text(text = "최대: ${sliderValues.endInclusive.toInt()}원", modifier = Modifier.padding(8.dp))
            }

            RangeSlider(
                value = sliderValues,
                onValueChange = { values ->
                    sliderValues = values
                    updateMinMaxPrice(values.start.toInt(), values.endInclusive.toInt())
                },
                onValueChangeFinished = {
                    onPriceSliderValueChangeFinished()
                },
                valueRange = 1000f..100000f,
                steps = 199,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Composable
fun FavoriteTopBar(
    onClickBackIcon: () -> Unit,
    onClickSortIcon: () -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {

        Row(
            modifier = modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                onClickBackIcon()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "arrow_icon",
                    modifier = modifier.size(24.dp),
                    tint = Gray700
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }

        Row(
            modifier = modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(id = R.string.favorite_book),
                style = BookTextStyles.subTitle1Bold(Black),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Row(
            modifier = modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                onClickSortIcon()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_sort),
                    contentDescription = "ic_sort",
                    modifier = modifier.size(24.dp),
                    tint = Gray700
                )
            }
        }

    }
}
