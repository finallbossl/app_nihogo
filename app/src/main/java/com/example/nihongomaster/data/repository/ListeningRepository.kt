package com.example.nihongomaster.data.repository

import com.example.nihongomaster.data.remote.ApiClient
import com.example.nihongomaster.data.remote.api.ListeningResponse
import com.example.nihongomaster.data.remote.api.ListeningDetailResponse
import com.example.nihongomaster.model.ListeningLesson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ListeningRepository {
    
    suspend fun getListening(): Result<List<ListeningLesson>> = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.apiService.getListening()
            if (response.isSuccessful) {
                val listeningList = response.body()?.map { it.toListeningLesson() } ?: emptyList()
                Result.success(listeningList)
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getListeningDetail(lessonId: String): Result<ListeningLesson> = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.apiService.getListeningDetail(lessonId)
            if (response.isSuccessful) {
                val lesson = response.body()?.toListeningLesson()
                if (lesson != null) {
                    Result.success(lesson)
                } else {
                    Result.failure(Exception("Lesson not found"))
                }
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun ListeningResponse.toListeningLesson(): ListeningLesson {
        return ListeningLesson(
            id = this.id,
            categoryId = "listening",
            title = this.title,
            level = this.level,
            thumbnailRes = null,
            audioDurationSec = this.duration,
            audioUrl = this.audioUrl
        )
    }
    
    private fun ListeningDetailResponse.toListeningLesson(): ListeningLesson {
        return ListeningLesson(
            id = this.id,
            categoryId = "listening",
            title = this.title,
            level = this.level,
            thumbnailRes = null,
            audioDurationSec = this.duration,
            audioUrl = this.audioUrl,
            transcript = this.transcript,
            description = this.description,
            questions = this.questions
        )
    }
}