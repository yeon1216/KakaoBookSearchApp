package com.example.searchbookapp.data.remote.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.searchbookapp.data.JobResult
import com.example.searchbookapp.data.local.source.BookInfoDao
import com.example.searchbookapp.data.model.Book
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject


class BookPagingSource @Inject constructor(
    private val bookRemoteSource: BookRemoteSource,
    private val bookInfoDao: BookInfoDao,
    private val query: String,
    private val sort: String? = "accuracy",
    private val size: Int? = 20,
    private val target: String? = null
) : PagingSource<Int, Book>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
        val page = params.key ?: 1
        val result = bookRemoteSource.searchBooks(
            query = query,
            sort = sort,
            page = page,
            size = size,
            target = target
        )
        return if (result is JobResult.Success) {
            val bookSearchResponse = result.data
            val books = coroutineScope {
                bookSearchResponse.documents.map { document ->
                    async {
                        val isFavorite = bookInfoDao.isBookExists(document.isbn)
                        Book(
                            isFavorite = isFavorite,
                            title = document.title,
                            contents = document.contents,
                            url = document.url,
                            isbn = document.isbn,
                            datetime = document.datetime,
                            authors = document.authors,
                            publisher = document.publisher,
                            translators = document.translators,
                            price = document.price,
                            salePrice = document.sale_price,
                            thumbnail = document.thumbnail,
                            status = document.status
                        )
                    }
                }.awaitAll()
            }
            LoadResult.Page(
                data = books,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (result.data.meta.is_end) null else page + 1
            )
        } else if (result is JobResult.Error) {
            LoadResult.Error(result.exception)
        } else {
            LoadResult.Error(Throwable())
        }
    }
    override fun getRefreshKey(state: PagingState<Int, Book>): Int? {
        return state.anchorPosition
    }
}