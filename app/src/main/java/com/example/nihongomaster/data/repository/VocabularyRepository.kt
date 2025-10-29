package com.example.nihongomaster.data.repository

import com.example.nihongomaster.data.remote.ApiClient
import com.example.nihongomaster.data.remote.api.VocabResponse
import com.example.nihongomaster.model.VocabCategory
import com.example.nihongomaster.model.VocabWord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VocabularyRepository {
    
    suspend fun getVocabulary(): Result<List<VocabCategory>> = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.apiService.getVocabulary()
            if (response.isSuccessful) {
                val vocabList = response.body()?.map { it.toVocabCategory() } ?: emptyList()
                Result.success(vocabList)
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getVocabWords(categoryId: String): Result<List<VocabWord>> = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.apiService.getVocabWords(categoryId)
            if (response.isSuccessful) {
                val wordList = response.body()?.map { it.toVocabWord() } ?: emptyList()
                Result.success(wordList)
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun VocabResponse.toVocabCategory(): VocabCategory {
        return VocabCategory(
            id = this.id,
            iconRes = com.example.nihongomaster.R.drawable.ic_vocab_box,
            title = this.title,
            subtitle = "${this.wordCount} từ vựng",
            progress = 0.0f
        )
    }
    
    private fun com.example.nihongomaster.data.remote.api.VocabWordResponse.toVocabWord(): VocabWord {
        return VocabWord(
            id = this.id,
            word = this.kanji,
            reading = this.hiragana,
            meaning = this.meaning,
            level = this.level,
            isFavorite = false,
            category = this.categoryId
        )
    }
}