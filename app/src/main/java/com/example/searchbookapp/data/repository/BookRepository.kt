package com.example.searchbookapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.searchbookapp.data.JobResult
import com.example.searchbookapp.data.local.source.BookInfoDao
import com.example.searchbookapp.data.model.Book
import com.example.searchbookapp.data.remote.source.BookPagingSource
import com.example.searchbookapp.data.remote.source.BookRemoteSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(
    private val bookRemoteSource: BookRemoteSource,
    private val bookInfoDao: BookInfoDao
) {
    companion object {
        private const val PAGE_SIZE = 20
    }


    private val bookPagingFlow = MutableStateFlow<PagingData<Book>>(PagingData.empty())
    fun getBookPagingFlow(): Flow<PagingData<Book>> = bookPagingFlow.asStateFlow()

    fun getBooks(
        query: String,
        sort: String? = "accuracy",
        target: String? = null
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            Pager(PagingConfig(pageSize = PAGE_SIZE)) {
                BookPagingSource(
                    bookRemoteSource, bookInfoDao, query, sort, PAGE_SIZE, target
                )
            }.flow.collect { pagingData ->
                bookPagingFlow.value = pagingData
            }
        }
    }

    suspend fun addFavoriteBook(
        book: Book
    ): JobResult<Book> {
        book.isFavorite = true
        return try {
            bookInfoDao.insert(book)
            JobResult.Success(book)
        } catch (e: Exception) {
            println("Failed to insert book: ${e.message}")
            JobResult.Error(e)
        }
    }

    suspend fun deleteFavoriteBook(
        book: Book
    ): JobResult<Book> {
        book.isFavorite = false
        return try {
            bookInfoDao.delete(book)
            JobResult.Success(book)
        } catch (e: Exception) {
            println("Failed to insert book: ${e.message}")
            JobResult.Error(e)
        }
    }

    suspend fun searchBooks(
        query: String,
        sort: String? = "accuracy",
        page: Int? = 1,
        size: Int? = 20,
        target: String? = null
    ): JobResult<List<Book>> {
        println("searchBooks START")
        val response = bookRemoteSource.searchBooks(
            query = query,
            sort = sort,
            page = page,
            size = size,
            target = target
        )
        if (response is JobResult.Success) {
            val bookSearchResponse = response.data
            val books = coroutineScope {
                bookSearchResponse.documents.map { document ->
                    async {
                        val result = isFavoriteBook(document.isbn)
                        var isFavorite = false
                        if (result is JobResult.Success) {
                            if (result.data) {
                                isFavorite = true
                            }
                        }
                        println("searchBooks book: ${document.title}, favorite : $isFavorite")
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
            println("searchBooks END")
            return JobResult.Success(books)
        } else {
            println("searchBooks END 2")
            return response as JobResult.Error
        }
    }

    private suspend fun isFavoriteBook(
        isbn: String
    ): JobResult<Boolean> {
        return try {
            val isFavoriteBook = bookInfoDao.isBookExists(isbn)
            JobResult.Success(isFavoriteBook)
        } catch (e: Exception) {
            println("Failed to insert book: ${e.message}")
            JobResult.Error(e)
        }
    }

    suspend fun getAllFavoriteBooks(): JobResult<List<Book>> {
        return try {
            val bookList = bookInfoDao.getAllBooks()
            JobResult.Success(bookList)
        } catch (e: Exception) {
            println("Failed to insert book: ${e.message}")
            JobResult.Error(e)
        }
    }

    suspend fun getBooksSortedByTitleAsc(): JobResult<List<Book>> {
        return try {
            val bookList = bookInfoDao.getBooksSortedByTitleAsc()
            JobResult.Success(bookList)
        } catch (e: Exception) {
            println("Failed to insert book: ${e.message}")
            JobResult.Error(e)
        }
    }

    suspend fun getBooksSortedByTitleDesc(): JobResult<List<Book>> {
        return try {
            val bookList = bookInfoDao.getBooksSortedByTitleDesc()
            JobResult.Success(bookList)
        } catch (e: Exception) {
            println("Failed to insert book: ${e.message}")
            JobResult.Error(e)
        }
    }

    suspend fun getBooksByPriceRangeSortedByTitleAsc(minPrice: Int, maxPrice: Int): JobResult<List<Book>> {
        return try {
            val bookList = bookInfoDao.getBooksByPriceRangeSortedByTitleAsc(minPrice, maxPrice)
            JobResult.Success(bookList)
        } catch (e: Exception) {
            println("Failed to insert book: ${e.message}")
            JobResult.Error(e)
        }
    }

    suspend fun getBooksByPriceRangeSortedByTitleDesc(minPrice: Int, maxPrice: Int): JobResult<List<Book>> {
        return try {
            val bookList = bookInfoDao.getBooksByPriceRangeSortedByTitleDesc(minPrice, maxPrice)
            JobResult.Success(bookList)
        } catch (e: Exception) {
            println("Failed to insert book: ${e.message}")
            JobResult.Error(e)
        }
    }

    suspend fun getBooksByAuthor(authorName: String): JobResult<List<Book>> {
        return try {
            val bookList = bookInfoDao.getBooksByAuthor(authorName)
            JobResult.Success(bookList)
        } catch (e: Exception) {
            println("Failed to insert book: ${e.message}")
            JobResult.Error(e)
        }
    }

}