package com.example.nihongomaster.data.local.dao

import androidx.room.*
import com.example.nihongomaster.data.local.entities.VocabWordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VocabWordDao {

    @Query("SELECT * FROM vocab_words WHERE categoryId = :categoryId")
    fun getWordsByCategory(categoryId: String): Flow<List<VocabWordEntity>>

    @Query("SELECT * FROM vocab_words WHERE isFavorite = 1")
    fun getFavoriteWords(): Flow<List<VocabWordEntity>>

    @Query("SELECT * FROM vocab_words WHERE id = :wordId")
    suspend fun getWordById(wordId: String): VocabWordEntity?

    @Query("UPDATE vocab_words SET isFavorite = :isFavorite WHERE id = :wordId")
    suspend fun updateFavoriteStatus(wordId: String, isFavorite: Boolean)

    @Query("UPDATE vocab_words SET isLearned = :isLearned WHERE id = :wordId")
    suspend fun updateLearnedStatus(wordId: String, isLearned: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWords(words: List<VocabWordEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: VocabWordEntity)

    @Delete
    suspend fun deleteWord(word: VocabWordEntity)
}