package com.example.searchbookapp.ui.book

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.PagingState
import androidx.paging.cachedIn
import com.example.searchbookapp.R
import com.example.searchbookapp.data.JobResult
import com.example.searchbookapp.data.model.Book
import com.example.searchbookapp.data.repository.BookRepository
import com.example.searchbookapp.ui.base.BaseViewModel
import com.example.searchbookapp.ui.base.LoadState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.searchbookapp.ui.book.BookContract.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltViewModel
class BookViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : BaseViewModel<BookViewState, BookSideEffect, BookEvent>(BookViewState()) {
    override fun handleEvents(event: BookEvent) {
        when (event) {
            is BookEvent.InitScreen -> {
                searchBook("미움받을 용기")
            }
            is BookEvent.OnSearch -> {
                if (event.query.length >= 2) {
                    searchBook(event.query)
                } else {
                    sendEffect({ BookSideEffect.ShowSnackBar(R.string.input_validation_message) })
                }
            }
            is BookEvent.OnClickSearchBar -> { updateSearchFocus() }
            is BookEvent.OnClickSearchCloseIcon -> { updateSearchFocus() }
            is BookEvent.OnClickBackIcon -> {
                sendEffect({ BookSideEffect.NavigateUp })
            }
            is BookEvent.OnClickFavoriteIcon -> {
                onClickFavoriteIcon(event.book)
            }
            is BookEvent.OnClickBookItem -> {
                updateState { copy(isDetailBook = event.book) }
            }
            is BookEvent.OnClickDetailBookCloseIcon -> {
                updateState { copy(isDetailBook = null) }
            }
        }
    }

    private val _bookPagingData = MutableStateFlow<PagingData<Book>>(PagingData.empty())
    val bookPagingFlow: StateFlow<PagingData<Book>> = _bookPagingData.asStateFlow()

    init {
        viewModelScope.launch {
            bookRepository.getBookPagingFlow().cachedIn(viewModelScope).collectLatest { pagingData ->
                _bookPagingData.value = pagingData
            }
        }
    }

    private fun onClickFavoriteIcon(book: Book) = viewModelScope.launch {
        if (!book.isFavorite) {
            val result = bookRepository.addFavoriteBook(book)
            if (result is JobResult.Success) {
                bookRepository.getBooks(query = viewState.value.searchBook)
                sendEffect({ BookSideEffect.ShowSnackBar(R.string.add_favorite_msg) })
            }
        } else {
            val result = bookRepository.deleteFavoriteBook(book)
            if (result is JobResult.Success) {
                bookRepository.getBooks(query = viewState.value.searchBook)
                sendEffect({ BookSideEffect.ShowSnackBar(R.string.delete_favorite_msg) })
            }
        }
    }

    private fun updateSearchFocus() {
        if (viewState.value.isSearchFocused) {
            updateState { copy(isSearchFocused = false) }
        } else {
            updateState { copy(isSearchFocused = true) }
        }
    }

    private fun searchBook(query: String) = viewModelScope.launch {
        updateState { copy(loadState = LoadState.LOADING, isSearchFocused = false, searchBook = query) }
        println("ksy : searchBook: $query")
        try {
            bookRepository.getBooks(query = query)
            updateState { copy(loadState = LoadState.SUCCESS) }
        } catch (e: Exception) {
            updateState { copy(loadState = LoadState.ERROR, error = e, searchBook = "") }
        }
    }

}
