package com.example.searchbookapp.data.local.source

import androidx.room.*
import com.example.searchbookapp.data.model.Book

@Dao
interface BookInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: Book)

    @Delete
    suspend fun delete(book: Book)

    // 해당 책이 db에 존재하는지 확인
    @Query("SELECT EXISTS(SELECT 1 FROM Book WHERE isbn = :isbn)")
    suspend fun isBookExists(isbn: String): Boolean

    // 모든 도서 조회
    @Query("SELECT * FROM Book")
    suspend fun getAllBooks(): List<Book>

    // 도서 제목 오름차순 정렬
    @Query("SELECT * FROM Book ORDER BY title ASC")
    suspend fun getBooksSortedByTitleAsc(): List<Book>

    // 도서 제목 내림차순 정렬
    @Query("SELECT * FROM Book ORDER BY title DESC")
    suspend fun getBooksSortedByTitleDesc(): List<Book>

    // 가격 필터 기능 (minPrice 이상 maxPrice 이하의 도서 검색)
    @Query("SELECT * FROM Book WHERE price BETWEEN :minPrice AND :maxPrice")
    suspend fun getBooksByPriceRange(minPrice: Int, maxPrice: Int): List<Book>

    // 가격 필터 기능 (minPrice 이상 maxPrice 이하의 도서 검색, 제목 오름차순 정렬)
    @Query("SELECT * FROM Book WHERE price BETWEEN :minPrice AND :maxPrice ORDER BY title ASC")
    suspend fun getBooksByPriceRangeSortedByTitleAsc(minPrice: Int, maxPrice: Int): List<Book>

    // 가격 필터 기능 (minPrice 이상 maxPrice 이하의 도서 검색, 제목 내림차순 정렬)
    @Query("SELECT * FROM Book WHERE price BETWEEN :minPrice AND :maxPrice ORDER BY title DESC")
    suspend fun getBooksByPriceRangeSortedByTitleDesc(minPrice: Int, maxPrice: Int): List<Book>

    // 저자 검색 기능
    @Query("SELECT * FROM Book WHERE :authorName IN (authors)")
    suspend fun getBooksByAuthor(authorName: String): List<Book>
}