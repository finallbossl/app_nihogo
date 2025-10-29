package com.example.nihongomaster.model

data class User(
    val uid: String = "",
    val email: String? = null,
    val displayName: String? = null,
    val photoUrl: String? = null,
    val lastLoginAt: Long = 0L,
    val profile: UserProfile = UserProfile(),
    val progress: UserProgress = UserProgress()
)

data class UserProfile(
    val joinedAt: Long = 0L,
    val level: String = "N5",
    val totalWordsLearned: Int = 0,
    val currentStreak: Int = 0,
    val totalStudyTime: Long = 0L
)

data class UserProgress(
    val vocabCompleted: Int = 0,
    val readingCompleted: Int = 0,
    val listeningCompleted: Int = 0,
    val testsCompleted: Int = 0,
    val totalScore: Int = 0,
    val lastActivity: Long = 0L
)