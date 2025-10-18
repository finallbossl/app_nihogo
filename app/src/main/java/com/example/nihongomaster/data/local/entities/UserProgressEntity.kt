package com.example.nihongomaster.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey val id: Int = 1,
    val wordsLearned: Int = 0,
    val lessonsCompleted: Int = 0,
    val studyTimeMinutes: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val accuracy: Float = 0f,
    val level: String = "Beginner",
    val xp: Int = 0,
    val lastStudyDate: Long = 0,
    val updatedAt: Long = System.currentTimeMillis()
)