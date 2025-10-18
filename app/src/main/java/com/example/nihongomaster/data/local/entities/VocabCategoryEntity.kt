package com.example.nihongomaster.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vocab_categories")
data class VocabCategoryEntity(
    @PrimaryKey val id: String,
    val title: String,
    val subtitle: String,
    val iconRes: Int,
    val progress: Float = 0f,
    val totalWords: Int = 0,
    val learnedWords: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)