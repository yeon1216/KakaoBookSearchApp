package com.example.searchbookapp.data.remote.source

import com.example.searchbookapp.data.JobResult
import com.example.searchbookapp.data.remote.api.BookService
import com.example.searchbookapp.data.remote.api.handleApi
import com.example.searchbookapp.data.remote.dto.BookSearchResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRemoteSource @Inject constructor(
    private val bookService: BookService
) {
    suspend fun searchBooks(
        query: String,
        sort: String? = "accuracy",
        page: Int? = 1,
        size: Int? = 10,
        target: String? = null
    ): JobResult<BookSearchResponse> = handleApi {
        println("ksy : query: $query, page: $page, size: $size")
        bookService.searchBooks(
            query = query,
            sort = sort,
            page = page,
            size = size,
            target = target
        )
    }
}