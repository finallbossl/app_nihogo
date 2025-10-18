package com.example.nihongomaster.data.repository

import com.example.nihongomaster.data.local.dao.UserProgressDao
import com.example.nihongomaster.data.local.entities.UserProgressEntity
import com.example.nihongomaster.data.remote.api.NihongoApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userProgressDao: UserProgressDao, private val apiService: NihongoApiService
) {

    fun getUserProgress(): Flow<UserProgressEntity?> {
        return userProgressDao.getUserProgress()
    }

    suspend fun incrementWordsLearned() {
        userProgressDao.incrementWordsLearned()
        syncProgressToServer()
    }

    suspend fun incrementLessonsCompleted() {
        userProgressDao.incrementLessonsCompleted()
        syncProgressToServer()
    }

    suspend fun addStudyTime(minutes: Int) {
        userProgressDao.addStudyTime(minutes)
        syncProgressToServer()
    }

    suspend fun updateStreak(streak: Int, date: Long) {
        userProgressDao.updateStreak(streak, date)
        syncProgressToServer()
    }

    suspend fun initializeUser() {
        val defaultProgress = UserProgressEntity()
        userProgressDao.insertProgress(defaultProgress)
    }

    private suspend fun syncProgressToServer() {
        try {
            // TODO: Implement when backend is ready
            // val progress = userProgressDao.getUserProgress().first()
            // progress?.let { apiService.syncUserProgress(it.toDto()) }
        } catch (e: Exception) {
            // Handle sync error
        }
    }

    suspend fun syncFromServer() {
        try {
            // TODO: Implement when backend is ready
            // val response = apiService.getUserProgress()
            // if (response.isSuccessful) {
            //     response.body()?.let { dto ->
            //         userProgressDao.insertProgress(dto.toEntity())
            //     }
            // }
        } catch (e: Exception) {
            // Handle network error
        }
    }
}