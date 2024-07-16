package com.example.searchbookapp.di

import com.example.searchbookapp.data.remote.api.BookService
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(HttpRoutes.getBaseUrl())
            .addConverterFactory(gsonConverterFactory)
            .build()

    @Singleton
    @Provides
    fun provideBookService(
        retrofit: Retrofit,
    ): BookService {
        return retrofit.create(BookService::class.java)
    }

}

object HttpRoutes {

    private const val KAKAO_BASE_URL = "https://dapi.kakao.com"
    fun getBaseUrl(): String {
        return KAKAO_BASE_URL
    }

}

