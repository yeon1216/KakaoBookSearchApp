package com.example.searchbookapp.ui.main

import com.example.searchbookapp.R
import com.example.searchbookapp.data.repository.BookRepository
import com.example.searchbookapp.ui.base.BaseViewModel
import com.example.searchbookapp.ui.book.BookContract
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.searchbookapp.ui.main.MainContract.*

@HiltViewModel
class MainViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : BaseViewModel<MainViewState, MainSideEffect, MainEvent>(
    MainViewState()
) {
    override fun handleEvents(event: MainEvent) {
        when (event) {
            is MainEvent.InitScreen -> {

            }
            is MainEvent.OnClickSearchBookBtn -> {
                sendEffect({ MainSideEffect.NavigateToSearchBook })
            }
            is MainEvent.OnClickFavoriteBookBtn -> {
                sendEffect({ MainSideEffect.NavigateToFavorite })
            }
            else -> {}
        }
    }
}
