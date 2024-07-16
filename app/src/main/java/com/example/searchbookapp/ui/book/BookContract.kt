package com.example.searchbookapp.ui.book

import com.example.searchbookapp.data.model.Book
import com.example.searchbookapp.ui.base.LoadState
import com.example.searchbookapp.ui.base.ViewEvent
import com.example.searchbookapp.ui.base.ViewSideEffect
import com.example.searchbookapp.ui.base.ViewState

class BookContract {

    data class BookViewState(
        val loadState: LoadState = LoadState.SUCCESS,
        val searchBook: String = "미움받을 용기",
        val error: Throwable? = null,
        val isSearchFocused: Boolean = false,
        val isDetailBook: Book? = null,
        val page: Int = 1,
        val isLastPage: Boolean = false
    ) : ViewState

    sealed class BookSideEffect: ViewSideEffect {
        object NavigateUp : BookSideEffect()
        data class ShowSnackBar(val resId: Int) : BookSideEffect()
    }

    sealed class BookEvent: ViewEvent {
        object InitScreen: BookEvent()
        data class OnSearch(val query: String): BookEvent()
        object OnClickSearchBar: BookEvent()
        object OnClickSearchCloseIcon: BookEvent()
        data class OnClickBookItem(val book: Book): BookEvent()
        data class OnClickFavoriteIcon(val book: Book): BookEvent()
        object OnClickDetailBookCloseIcon: BookEvent()
        object OnClickBackIcon: BookEvent()
    }

}