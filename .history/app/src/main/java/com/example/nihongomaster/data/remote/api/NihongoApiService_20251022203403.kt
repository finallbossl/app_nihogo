package com.example.nihongomaster.data.remote.api

import com.example.nihongomaster.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface NihongoApiService {

    // Vocabulary endpoints
    @GET("vocabulary/categories")
    suspend fun getVocabCategories(): Response<List<VocabCategoryDto>>

    @GET("vocabulary/words/{categoryId}")
    suspend fun getWordsByCategory(@Path("categoryId") categoryId: String): Response<List<VocabWordDto>>

    @GET("vocabulary/word/{wordId}")
    suspend fun getWordDetail(@Path("wordId") wordId: String): Response<VocabWordDetailDto>

    // User progress endpoints
    @GET("user/progress")
    suspend fun getUserProgress(): Response<UserProgressDto>

    @POST("user/progress")
    suspend fun syncUserProgress(@Body progress: UserProgressDto): Response<Unit>

    @POST("user/favorites")
    suspend fun syncFavorites(@Body favorites: List<String>): Response<Unit>

    @GET("user/favorites")
    suspend fun getFavorites(): Response<List<String>>

    // Study session endpoints
    @POST("study/session")
    suspend fun recordStudySession(@Body session: StudySessionDto): Response<Unit>

    @GET("study/sessions")
    suspend fun getStudySessions(): Response<List<StudySessionDto>>

    // Authentication endpoints
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("auth/refresh")
    suspend fun refresh(@Body request: RefreshRequest): Response<AuthResponse>

    @POST("auth/logout")
    suspend fun logout(@Body request: LogoutRequest): Response<Unit>
}