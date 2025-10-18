package com.example.nihongomaster.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vocab_words")
data class VocabWordEntity(
    @PrimaryKey val id: String,
    val kanji: String,
    val hiragana: String,
    val meaning: String,
    val level: String,
    val categoryId: String,
    val isFavorite: Boolean = false,
    val isLearned: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)