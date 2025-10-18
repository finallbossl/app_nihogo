package com.example.nihongomaster.data.repository

import com.example.nihongomaster.data.local.dao.VocabCategoryDao
import com.example.nihongomaster.data.local.dao.VocabWordDao
import com.example.nihongomaster.data.local.entities.VocabCategoryEntity
import com.example.nihongomaster.data.local.entities.VocabWordEntity
import com.example.nihongomaster.data.remote.api.NihongoApiService
import com.example.nihongomaster.model.VocabCategory
import com.example.nihongomaster.model.VocabWord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VocabularyRepository @Inject constructor(
    private val vocabWordDao: VocabWordDao,
    private val vocabCategoryDao: VocabCategoryDao,
    private val apiService: NihongoApiService
) {

    fun getCategories(): Flow<List<VocabCategory>> {
        return vocabCategoryDao.getAllCategories().map { entities ->
            entities.map { it.toVocabCategory() }
        }
    }

    fun getWordsByCategory(categoryId: String): Flow<List<VocabWord>> {
        return vocabWordDao.getWordsByCategory(categoryId).map { entities ->
            entities.map { it.toVocabWord() }
        }
    }

    fun getFavoriteWords(): Flow<List<VocabWord>> {
        return vocabWordDao.getFavoriteWords().map { entities ->
            entities.map { it.toVocabWord() }
        }
    }

    suspend fun toggleFavorite(wordId: String) {
        val word = vocabWordDao.getWordById(wordId)
        word?.let {
            vocabWordDao.updateFavoriteStatus(wordId, !it.isFavorite)
        }
    }

    suspend fun markWordAsLearned(wordId: String) {
        vocabWordDao.updateLearnedStatus(wordId, true)
    }

    suspend fun syncCategories() {
        try {
            val response = apiService.getVocabCategories()
            if (response.isSuccessful) {
                response.body()?.let { categories ->
                    // TODO: Add mapping when DTO is ready
                    // val entities = categories.map { it.toEntity() }
                    // vocabCategoryDao.insertCategories(entities)
                }
            }
        } catch (e: Exception) {
            // Handle network error - use local data
        }
    }

    suspend fun syncWords(categoryId: String) {
        try {
            val response = apiService.getWordsByCategory(categoryId)
            if (response.isSuccessful) {
                response.body()?.let { words ->
                    // TODO: Add mapping when DTO is ready
                    // val entities = words.map { it.toEntity() }
                    // vocabWordDao.insertWords(entities)
                }
            }
        } catch (e: Exception) {
            // Handle network error - use local data
        }
    }
}

// Extension functions for mapping
private fun VocabCategoryEntity.toVocabCategory() = VocabCategory(
    id = id, iconRes = iconRes, title = title, subtitle = subtitle, progress = progress
)

private fun VocabWordEntity.toVocabWord() = VocabWord(
    id = id,
    kanji = kanji,
    hiragana = hiragana,
    meaning = meaning,
    level = level,
    isFavorite = isFavorite
)