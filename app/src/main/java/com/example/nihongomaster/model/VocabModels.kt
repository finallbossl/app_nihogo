package com.example.nihongomaster.model

data class VocabCategory(
    val id: String,
    val iconRes: Int,
    val title: String,
    val subtitle: String,
    val progress: Float
)

data class VocabWord(
    val id: String,
    val kanji: String,
    val hiragana: String,
    val meaning: String,
    val level: String,
    val isFavorite: Boolean = false
)