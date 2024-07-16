package com.example.searchbookapp.ui.main

import com.example.searchbookapp.data.model.Book
import com.example.searchbookapp.ui.base.LoadState
import com.example.searchbookapp.ui.base.ViewEvent
import com.example.searchbookapp.ui.base.ViewSideEffect
import com.example.searchbookapp.ui.base.ViewState
import com.example.searchbookapp.ui.book.BookContract

class MainContract {

    data class MainViewState(
        val loadState: LoadState = LoadState.SUCCESS,
        val bookList: List<Book> = emptyList(),
        val searchBook: String = "",
        val error: Throwable? = null,
        val isSearchFocused: Boolean = false
    ) : ViewState

    sealed class MainSideEffect: ViewSideEffect {
        object NavigateToSearchBook : MainSideEffect()
        object NavigateToFavorite : MainSideEffect()
    }

    sealed class MainEvent: ViewEvent {
        object InitScreen: MainEvent()
        object OnClickSearchBookBtn: MainEvent()
        object OnClickFavoriteBookBtn: MainEvent()
    }

}