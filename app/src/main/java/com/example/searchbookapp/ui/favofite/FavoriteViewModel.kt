package com.example.searchbookapp.ui.favofite

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.searchbookapp.R
import com.example.searchbookapp.data.JobResult
import com.example.searchbookapp.data.model.Book
import com.example.searchbookapp.data.onError
import com.example.searchbookapp.data.onSuccess
import com.example.searchbookapp.data.repository.BookRepository
import com.example.searchbookapp.ui.base.BaseViewModel
import com.example.searchbookapp.ui.base.LoadState
import com.example.searchbookapp.ui.book.BookContract
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.searchbookapp.ui.favofite.FavoriteContract.*
import kotlinx.coroutines.launch

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : BaseViewModel<FavoriteViewState, FavoriteSideEffect, FavoriteEvent>(
    FavoriteViewState()
) {
    override fun handleEvents(event: FavoriteEvent) {
        when (event) {
            is FavoriteEvent.InitScreen -> {
                getBooksByPriceRangeSortedByTitle()
            }
            is FavoriteEvent.OnClickBackIcon -> {
                sendEffect({ FavoriteSideEffect.NavigateUp })
            }
            is FavoriteEvent.OnClickFavoriteIcon -> {
                onClickFavoriteIcon(event.book)
            }
            is FavoriteEvent.OnClickBookItem -> {
            }
            is FavoriteEvent.OnClickDetailBookCloseIcon -> {
            }
            is FavoriteEvent.OnClickSortIcon -> {
                updateState { copy(isASC = viewState.value.isASC.not()) }
                getBooksByPriceRangeSortedByTitle()
            }
            is FavoriteEvent.UpdateMinMaxPrice -> {
                updateState { copy(minPrice = event.min, maxPrice = event.max) }
            }
            is FavoriteEvent.OnPriceSliderValueChangeFinished -> {
                getBooksByPriceRangeSortedByTitle()
            }
        }
    }

    private fun onClickFavoriteIcon(book: Book) = viewModelScope.launch {
        val result = bookRepository.deleteFavoriteBook(book)
        if (result is JobResult.Success) {
            deleteBook(isbn = book.isbn)
            sendEffect({ FavoriteSideEffect.ShowSnackBar(R.string.delete_favorite_msg) })
        }
    }

    private fun updateFavoriteStatus(isbn: String, isFavorite: Boolean) = viewModelScope.launch {
        val tempBookList = viewState.value.bookList
        val updatedBookList = tempBookList.map { book ->
            if (book.isbn == isbn) {
                book.copy(isFavorite = isFavorite)
            } else {
                book
            }
        }
        updateState { copy(bookList = updatedBookList) }
    }
    private fun deleteBook(isbn: String) = viewModelScope.launch {
        val tempBookList = viewState.value.bookList
        val updatedBookList = tempBookList.filter { it.isbn != isbn }
        updateState { copy(bookList = updatedBookList) }
    }

    private fun getBooksByPriceRangeSortedByTitle() = viewModelScope.launch {
        updateState { copy(loadState = LoadState.LOADING) }
        val result = if (viewState.value.isASC) {
            bookRepository.getBooksByPriceRangeSortedByTitleAsc(viewState.value.minPrice, viewState.value.maxPrice)
        } else {
            bookRepository.getBooksByPriceRangeSortedByTitleDesc(viewState.value.minPrice, viewState.value.maxPrice)
        }
        result
            .onSuccess { bookList ->
                Log.d("ksy", "bookList : $bookList")
                updateState { copy(loadState = LoadState.SUCCESS, bookList = bookList) }
            }
            .onError { exception ->
                Log.d("ksy", "error $exception")
                updateState { copy(loadState = LoadState.ERROR, error = exception) }
            }
    }

}