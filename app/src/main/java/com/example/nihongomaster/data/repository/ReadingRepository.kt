package com.example.nihongomaster.data.repository

import com.example.nihongomaster.data.remote.ApiClient
import com.example.nihongomaster.data.remote.api.ReadingResponse
import com.example.nihongomaster.model.ReadingArticle
import com.example.nihongomaster.model.ReadingCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReadingRepository {
    
    suspend fun getReadingCategories(): Result<List<ReadingCategory>> = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.apiService.getReading()
            if (response.isSuccessful) {
                val categoryList = response.body()?.map { it.toReadingCategory() } ?: emptyList()
                Result.success(categoryList)
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getReadingArticles(categoryId: String): Result<List<ReadingArticle>> = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.apiService.getReadingArticles(categoryId)
            if (response.isSuccessful) {
                val articleList = response.body()?.map { it.toReadingArticle() } ?: emptyList()
                Result.success(articleList)
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun ReadingResponse.toReadingCategory(): ReadingCategory {
        return ReadingCategory(
            id = this.id,
            title = this.title,
            subtitle = "${this.articleCount} bài đọc",
            progress = 0.0f
        )
    }
    
    private fun com.example.nihongomaster.data.remote.api.ReadingArticleResponse.toReadingArticle(): ReadingArticle {
        return ReadingArticle(
            id = this.id,
            categoryId = this.categoryId,
            title = this.title,
            jpText = this.content
        )
    }
}