package com.example.nihongomaster.model

data class ProgressMetrics(
    val streakDays: Int,
    val wordsLearned: Int,
    val avgAccuracy: Int,
    val lessonsCompleted: Int,
    val studyTimeMinutes: Int
)

data class ActivityItem(
    val id: String,
    val iconRes: Int,
    val title: String,
    val subtitle: String,
    val timeAgo: String,
    val tag: String? = null
)

data class Achievement(
    val id: String,
    val iconRes: Int,
    val title: String,
    val subtitle: String,
    val achieved: Boolean
)

data class StudyModule(
    val id: String,
    val name: String,
    val iconRes: Int,
    val progress: Int,
    val totalItems: Int,
    val completedItems: Int
)

data class FavoriteWord(
    val id: String,
    val kanji: String,
    val hiragana: String,
    val meaning: String,
    val level: String
)

data class OngoingLesson(
    val id: String,
    val title: String,
    val type: String,
    val progress: Int,
    val iconRes: Int
)