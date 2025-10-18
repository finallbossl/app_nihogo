package com.example.nihongomaster.data.local.dao

import androidx.room.*
import com.example.nihongomaster.data.local.entities.UserProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProgressDao {

    @Query("SELECT * FROM user_progress WHERE id = 1")
    fun getUserProgress(): Flow<UserProgressEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: UserProgressEntity)

    @Query("UPDATE user_progress SET wordsLearned = wordsLearned + 1, updatedAt = :timestamp WHERE id = 1")
    suspend fun incrementWordsLearned(timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE user_progress SET lessonsCompleted = lessonsCompleted + 1, updatedAt = :timestamp WHERE id = 1")
    suspend fun incrementLessonsCompleted(timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE user_progress SET studyTimeMinutes = studyTimeMinutes + :minutes, updatedAt = :timestamp WHERE id = 1")
    suspend fun addStudyTime(minutes: Int, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE user_progress SET currentStreak = :streak, longestStreak = CASE WHEN :streak > longestStreak THEN :streak ELSE longestStreak END, lastStudyDate = :date, updatedAt = :timestamp WHERE id = 1")
    suspend fun updateStreak(streak: Int, date: Long, timestamp: Long = System.currentTimeMillis())
}