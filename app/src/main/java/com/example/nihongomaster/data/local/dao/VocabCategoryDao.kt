package com.example.nihongomaster.data.local.dao

import androidx.room.*
import com.example.nihongomaster.data.local.entities.VocabCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VocabCategoryDao {
    
    @Query("SELECT * FROM vocab_categories ORDER BY title")
    fun getAllCategories(): Flow<List<VocabCategoryEntity>>
    
    @Query("SELECT * FROM vocab_categories WHERE id = :categoryId")
    suspend fun getCategoryById(categoryId: String): VocabCategoryEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<VocabCategoryEntity>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: VocabCategoryEntity)
    
    @Query("UPDATE vocab_categories SET progress = :progress, learnedWords = :learnedWords WHERE id = :categoryId")
    suspend fun updateProgress(categoryId: String, progress: Float, learnedWords: Int)
}