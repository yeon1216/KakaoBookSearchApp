package com.example.searchbookapp.ui.book

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.searchbookapp.R
import com.example.searchbookapp.data.model.Book

@Composable
fun BookListView(
    bookList: List<Book>,
    onClickBookItem: (Book) -> Unit,
    onClickFavoriteIcon: (Book) -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        if (bookList.isEmpty()) {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(id = R.string.no_favorite_book))
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(bookList) { item ->
                    BookItem(
                        onClickBookItem = { book ->
                            onClickBookItem(book)
                        },
                        onClickFavoriteIcon = { book ->
                            onClickFavoriteIcon(book)
                        },
                        modifier = modifier,
                        book = item
                    )
                }
            }

        }
    }
}


@Composable
fun PagingBookListView(
    bookPagingItems: LazyPagingItems<Book>,
    onClickBookItem: (Book) -> Unit,
    onClickFavoriteIcon: (Book) -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        if (bookPagingItems.itemCount == 0) {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(id = R.string.no_book))
            }
        } else {
            LazyColumn {
                items(bookPagingItems.itemCount) { index ->
                    bookPagingItems[index]?.let { book ->
                        BookItem(
                            book = book,
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
}
