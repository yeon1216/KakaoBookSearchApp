package com.example.searchbookapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(
    @PrimaryKey
    val isbn: String,
    @ColumnInfo
    var isFavorite: Boolean,
    @ColumnInfo
    val title: String,
    @ColumnInfo
    val contents: String,
    @ColumnInfo
    val url: String,
    @ColumnInfo
    val datetime: String, // ISO 8601 형식
    @ColumnInfo
    val authors: List<String>,
    @ColumnInfo
    val publisher: String,
    @ColumnInfo
    val translators: List<String>,
    @ColumnInfo
    val price: Int,
    @ColumnInfo
    val salePrice: Int,
    @ColumnInfo
    val thumbnail: String,
    @ColumnInfo
    val status: String
)