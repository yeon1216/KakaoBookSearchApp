package com.example.searchbookapp.ui.favofite

import com.example.searchbookapp.data.model.Book
import com.example.searchbookapp.ui.base.LoadState
import com.example.searchbookapp.ui.base.ViewEvent
import com.example.searchbookapp.ui.base.ViewSideEffect
import com.example.searchbookapp.ui.base.ViewState
import com.example.searchbookapp.ui.book.BookContract

class FavoriteContract {

    data class FavoriteViewState(
        val loadState: LoadState = LoadState.SUCCESS,
        val bookList: List<Book> = emptyList(),
        val error: Throwable? = null,
        val isASC: Boolean = true,
        val minPrice: Int = 1000,
        val maxPrice: Int = 100000,
    ) : ViewState

    sealed class FavoriteSideEffect: ViewSideEffect {
        object NavigateUp : FavoriteSideEffect()
        data class ShowSnackBar(val resId: Int) : FavoriteSideEffect()
    }

    sealed class FavoriteEvent: ViewEvent {
        object InitScreen: FavoriteEvent()
        object OnClickBackIcon: FavoriteEvent()
        data class OnClickBookItem(val book: Book): FavoriteEvent()
        data class OnClickFavoriteIcon(val book: Book): FavoriteEvent()
        object OnClickDetailBookCloseIcon: FavoriteEvent()
        object OnClickSortIcon: FavoriteEvent()
        data class UpdateMinMaxPrice(val min: Int, val max: Int): FavoriteEvent()
        object OnPriceSliderValueChangeFinished: FavoriteEvent()
    }

}