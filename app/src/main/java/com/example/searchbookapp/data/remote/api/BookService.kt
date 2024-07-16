package com.example.searchbookapp.data.remote.api

import com.example.searchbookapp.BuildConfig
import com.example.searchbookapp.data.remote.dto.BookSearchResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface BookService {

    @GET("/v3/search/book")
    @Headers("Authorization: KakaoAK ${BuildConfig.KAKAO_API_TOKEN}")
    suspend fun searchBooks(
        @Query("query") query: String,
        @Query("sort") sort: String? = "accuracy",
        @Query("page") page: Int? = 1,
        @Query("size") size: Int? = 10,
        @Query("target") target: String? = null
    ): BookSearchResponse

}
