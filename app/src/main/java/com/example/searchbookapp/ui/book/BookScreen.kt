package com.example.searchbookapp.ui.book

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.searchbookapp.R
import com.example.searchbookapp.data.model.Book
import com.example.searchbookapp.ui.SearchBookAppState
import com.example.searchbookapp.ui.base.LoadState
import com.example.searchbookapp.ui.book.BookContract.*
import com.example.searchbookapp.ui.rememberSearchBookAppState
import com.example.searchbookapp.ui.theme.Black
import com.example.searchbookapp.ui.theme.BookTextStyles
import com.example.searchbookapp.ui.theme.Gray200
import com.example.searchbookapp.ui.theme.Gray500
import com.example.searchbookapp.ui.theme.Gray600
import com.example.searchbookapp.ui.theme.Gray700
import com.example.searchbookapp.ui.theme.Purple500
import com.example.searchbookapp.ui.theme.Red600
import com.example.searchbookapp.ui.theme.SearchBookTheme
import com.example.searchbookapp.ui.theme.Shapes
import com.example.searchbookapp.ui.theme.White
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@Composable
fun BookScreen(
    appState: SearchBookAppState,
    viewModel: BookViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()
    val bookPagingItems = viewModel.bookPagingFlow.collectAsLazyPagingItems()
    val scope = rememberCoroutineScope()

    Book(
        viewState = viewState,
        bookPagingItems = bookPagingItems,
        onSearch = { query ->
            viewModel.setEvent(BookEvent.OnSearch(query))
        },
        onClickSearchBar = {
            viewModel.setEvent(BookEvent.OnClickSearchBar)
        },
        onClickSearchCloseIcon = {
            viewModel.setEvent(BookEvent.OnClickSearchCloseIcon)
        },
        onClickBookItem = { book ->
            viewModel.setEvent(BookEvent.OnClickBookItem(book = book))
        },
        onClickDetailBookBackIcon = {
            viewModel.setEvent(BookEvent.OnClickDetailBookCloseIcon)
        },
        onClickBackIcon = {
            viewModel.setEvent(BookEvent.OnClickBackIcon)
        },
        onClickFavoriteIcon = { book ->
            viewModel.setEvent(BookEvent.OnClickFavoriteIcon(book = book))
        },
        modifier = Modifier
    )

    LaunchedEffect(key1 = viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is BookSideEffect.NavigateUp -> {
                    appState.upPress()
                }
                is BookSideEffect.ShowSnackBar -> {
                    scope.launch {
                        appState.showSnackbarMessage(effect.resId)
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.setEvent(BookEvent.InitScreen)
    }
}

@Composable
private fun Book(
    viewState: BookViewState,
    bookPagingItems: LazyPagingItems<Book>,
    onSearch: (query: String) -> Unit,
    onClickSearchBar: () -> Unit,
    onClickSearchCloseIcon: () -> Unit,
    onClickBookItem: (Book) -> Unit,
    onClickDetailBookBackIcon: () -> Unit,
    onClickBackIcon: () -> Unit,
    onClickFavoriteIcon: (Book) -> Unit,
    modifier: Modifier
) {

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        SearchBookContentView(
            viewState,
            bookPagingItems,
            onSearch,
            onClickSearchBar,
            onClickSearchCloseIcon,
            onClickBookItem,
            onClickBackIcon,
            onClickFavoriteIcon,
            modifier
        )
        if (viewState.isDetailBook != null) {
            BookDetail(
                onClickBackIcon = { onClickDetailBookBackIcon() },
                onClickFavoriteIcon = { book -> onClickFavoriteIcon(book) },
                book = viewState.isDetailBook,
                modifier = modifier
            )
        }

    }

}

@Composable
private fun SearchBookContentView(
    viewState: BookViewState,
    bookPagingItems: LazyPagingItems<Book>,
    onSearch: (query: String) -> Unit,
    onClickSearchBar: () -> Unit,
    onClickSearchCloseIcon: () -> Unit,
    onClickBookItem: (Book) -> Unit,
    onClickBackIcon: () -> Unit,
    onClickFavoriteIcon: (Book) -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 8.dp, bottom = 8.dp)
    ) {
        Column {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp)
            ) {
                Row {
                    if (!viewState.isSearchFocused) {
                        Box(
                            modifier = modifier
                                .height(56.dp)
                                .width(44.dp),
                            contentAlignment = Alignment.Center
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
                        }
                    }
                    Box(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .weight(1f)
                    ) {
                        if (viewState.isSearchFocused) {
                            SearchTextFieldView(
                                onSearch = { query -> onSearch(query) },
                                modifier = modifier
                            )
                        } else {
                            SearchView(
                                searchBook = viewState.searchBook,
                                modifier = modifier
                                    .clickable {
                                        onClickSearchBar()
                                    }
                            )
                        }
                    }
                    if (viewState.isSearchFocused) {
                        Box(
                            modifier = modifier.size(56.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(onClick = {
                                onClickSearchCloseIcon()
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "close_icon",
                                    modifier = modifier.size(24.dp),
                                    tint = Gray700
                                )
                            }
                        }
                    }
                }
            }
            when (viewState.loadState) {
                LoadState.SUCCESS -> {
                    Box(
                        modifier = modifier.fillMaxSize()
                    ) {
                        Column {
                            Box(
                                modifier = modifier
                                    .fillMaxSize()
                                    .weight(1f)
                            ) {
                                PagingBookListView(
                                    bookPagingItems = bookPagingItems,
                                    onClickBookItem = { book ->
                                        onClickBookItem(book)
                                    },
                                    onClickFavoriteIcon = { book ->
                                        onClickFavoriteIcon(book)
                                    },
                                    modifier = modifier
                                )
                                if (viewState.isSearchFocused) {
                                    Box(
                                        modifier = modifier
                                            .fillMaxSize()
                                            .background(White)
                                    )
                                }
                            }
                        }
                    }
                }
                LoadState.ERROR -> {
                    Box(
                        modifier = modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Error : ${viewState.error}",
                            style = BookTextStyles.subTitle2Regular(Black)
                        )
                    }
                }
                LoadState.LOADING-> {
                    Box(
                        modifier = modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = modifier.size(56.dp),
                            color = Purple500
                        )
                    }
                }
                else -> {
                    Box(modifier = modifier.fillMaxSize())
                }
            }
        }
    }
}


@Composable
fun SearchView(
    searchBook: String,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.fillMaxWidth()
        ) {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.CenterStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            BorderStroke(1.dp, Color.Gray),
                            shape = Shapes.medium
                        )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        if (searchBook == "") {
                            Text(
                                text = stringResource(id = R.string.search_book_placeholder),
                                style = BookTextStyles.captionRegular(Gray600)
                            )
                        } else {
                            Text(
                                text = searchBook,
                                style = BookTextStyles.subTitle2Regular(Black)
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "search_icon",
                            modifier = modifier
                        )
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchTextFieldView(
    onSearch: (query: String) -> Unit,
    modifier: Modifier
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    var queryValue by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        value = queryValue,
        onValueChange = {
            queryValue = it
        },
        singleLine = true,
        placeholder = {
            Text(
                text = stringResource(id = R.string.search_book_placeholder),
                style = BookTextStyles.captionRegular(Gray600)
            )
        },
        keyboardActions = KeyboardActions(onSearch = {
            onSearch(queryValue)
            keyboardController?.hide()
        }),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Purple500,
            unfocusedBorderColor = Gray500,
            cursorColor = Purple500
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
    )

}

@Preview
@Composable
private fun PreviewBookScreen() {
    SearchBookTheme {
        BookScreen(
            appState = rememberSearchBookAppState()
        )
    }
}
