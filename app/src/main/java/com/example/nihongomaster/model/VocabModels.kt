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
    val word: String,
    val reading: String,
    val meaning: String,
    val level: String,
    val isFavorite: Boolean = false,
    val category: String = "",
    val audioUrl: String? = null,
    val imageUrl: String? = null,
    val isLearned: Boolean = false
) {
    val kanji: String get() = word
    val hiragana: String get() = reading
}