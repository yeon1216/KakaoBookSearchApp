package com.example.searchbookapp.ui.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDate(dateString: String): String? {
    return try {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        val desiredFormat = SimpleDateFormat("yy.MM.dd", Locale.getDefault())
        val date: Date = originalFormat.parse(dateString) ?: return null
        desiredFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
