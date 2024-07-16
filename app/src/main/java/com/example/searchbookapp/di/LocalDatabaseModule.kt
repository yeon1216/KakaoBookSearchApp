package com.example.searchbookapp.di

import android.content.Context
import com.example.searchbookapp.data.local.source.BookInfoDao
import com.example.searchbookapp.data.local.source.SearchBookDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocalDatabaseModule {

    @Singleton
    @Provides
    fun provideSearchBookDatabase(@ApplicationContext applicationContext: Context): SearchBookDatabase {
        return SearchBookDatabase.getInstance(applicationContext)
    }

    @Provides
    fun provideBookInfoDao(db: SearchBookDatabase): BookInfoDao {
        return db.bookInfoDao()
    }

}
