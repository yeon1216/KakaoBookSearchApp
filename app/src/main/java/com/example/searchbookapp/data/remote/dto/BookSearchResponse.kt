package com.example.searchbookapp.data.remote.dto

data class BookSearchResponse(
    val meta: Meta,
    val documents: List<Document>
)

data class Meta(
    val total_count: Int,
    val pageable_count: Int,
    val is_end: Boolean
)

data class Document(
    val title: String,
    val contents: String,
    val url: String,
    val isbn: String,
    val datetime: String, // ISO 8601 형식
    val authors: List<String>,
    val publisher: String,
    val translators: List<String>,
    val price: Int,
    val sale_price: Int,
    val thumbnail: String,
    val status: String
)