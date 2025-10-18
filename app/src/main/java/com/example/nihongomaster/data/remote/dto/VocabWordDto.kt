package com.example.nihongomaster.data.remote.dto

data class VocabWordDto(
    val id: String,
    val kanji: String,
    val hiragana: String,
    val meaning: String,
    val level: String,
    val categoryId: String,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

data class VocabCategoryDto(
    val id: String,
    val title: String,
    val subtitle: String,
    val iconRes: Int,
    val totalWords: Int,
    val createdAt: String? = null
)

data class VocabWordDetailDto(
    val id: String,
    val kanji: String,
    val hiragana: String,
    val romaji: String,
    val ipa: String,
    val meaning: String,
    val definitionShort: String,
    val definitionLong: String,
    val level: String,
    val categoryId: String,
    val examples: List<ExampleSentenceDto>,
    val createdAt: String? = null
)

data class ExampleSentenceDto(
    val japanese: String, val english: String
)

data class UserProgressDto(
    val wordsLearned: Int,
    val lessonsCompleted: Int,
    val studyTimeMinutes: Int,
    val currentStreak: Int,
    val longestStreak: Int,
    val accuracy: Float,
    val level: String,
    val xp: Int,
    val lastStudyDate: String,
    val updatedAt: String
)

data class StudySessionDto(
    val id: String,
    val categoryId: String,
    val wordsStudied: Int,
    val correctAnswers: Int,
    val totalAnswers: Int,
    val durationMinutes: Int,
    val completedAt: String
)