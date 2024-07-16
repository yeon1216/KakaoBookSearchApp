package com.example.searchbookapp.data.local.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.searchbookapp.data.model.Book
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Database(entities = [Book::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class SearchBookDatabase: RoomDatabase() {
    abstract fun bookInfoDao(): BookInfoDao

    companion object {

        private const val DATABASE_NAME = "nenio"

        @Volatile private var instance: SearchBookDatabase? = null

        fun getInstance(context: Context): SearchBookDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): SearchBookDatabase {
            return Room.databaseBuilder(context, SearchBookDatabase::class.java, DATABASE_NAME)
                .build()
        }

    }

}

class Converters {
    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return Gson().toJson(list)
    }
}
