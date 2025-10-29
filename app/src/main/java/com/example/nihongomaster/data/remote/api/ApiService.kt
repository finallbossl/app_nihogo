package com.example.nihongomaster.data.remote.api

import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    @GET("api/admin/vocab/categories")
    suspend fun getVocabulary(): Response<List<VocabResponse>>
    
    @GET("api/admin/vocab/words/{categoryId}")
    suspend fun getVocabWords(@Path("categoryId") categoryId: String): Response<List<VocabWordResponse>>
    
    @GET("api/admin/reading/categories")
    suspend fun getReading(): Response<List<ReadingResponse>>
    
    @GET("api/admin/reading/articles/{categoryId}")
    suspend fun getReadingArticles(@Path("categoryId") categoryId: String): Response<List<ReadingArticleResponse>>
    
    @GET("api/admin/listening/lessons")
    suspend fun getListening(): Response<List<ListeningResponse>>
    
    @GET("api/admin/listening/lessons/{lessonId}")
    suspend fun getListeningDetail(@Path("lessonId") lessonId: String): Response<ListeningDetailResponse>
    
    @GET("api/admin/exercises")
    suspend fun getExercises(): Response<List<ExerciseResponse>>
}

data class VocabResponse(
    val id: String,
    val title: String,
    val description: String,
    val level: String,
    val wordCount: Int
)

data class VocabWordResponse(
    val id: String,
    val kanji: String,
    val hiragana: String,
    val meaning: String,
    val level: String,
    val categoryId: String,
    val audioUrl: String?,
    val exampleSentence: String?
)

data class ReadingResponse(
    val id: String,
    val title: String,
    val description: String,
    val level: String,
    val articleCount: Int
)

data class ReadingArticleResponse(
    val id: String,
    val title: String,
    val content: String,
    val categoryId: String,
    val level: String,
    val estimatedTime: Int,
    val imageUrl: String?
)

data class ListeningResponse(
    val id: String,
    val title: String,
    val audioUrl: String,
    val transcript: String,
    val level: String,
    val duration: Int
)

data class ListeningDetailResponse(
    val id: String,
    val title: String,
    val description: String,
    val audioUrl: String,
    val transcript: String,
    val level: String,
    val duration: Int,
    val questions: List<QuestionResponse>,
    val imageUrl: String?
)

data class ExerciseResponse(
    val id: String,
    val title: String,
    val description: String,
    val level: String,
    val questions: List<QuestionResponse>
)

data class QuestionResponse(
    val id: String? = null,
    val question: String? = null,
    val text: String? = null,
    val difficulty: String? = null,
    val options: List<String>,
    val correctAnswer: Int? = null,
    val correctIndex: Int? = null
)