package com.example.nihongomaster.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.nihongomaster.data.local.dao.*
import com.example.nihongomaster.data.local.entities.*

@Database(
    entities = [
        VocabWordEntity::class,
        VocabCategoryEntity::class,
        UserProgressEntity::class,
        TokenEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class NihongoDatabase : RoomDatabase() {

    abstract fun vocabWordDao(): VocabWordDao
    abstract fun vocabCategoryDao(): VocabCategoryDao
    abstract fun userProgressDao(): UserProgressDao
    abstract fun tokenDao(): TokenDao

    companion object {
        @Volatile
        private var INSTANCE: NihongoDatabase? = null

        fun getDatabase(context: Context): NihongoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NihongoDatabase::class.java,
                    "nihongo_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}