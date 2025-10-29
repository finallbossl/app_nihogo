package com.example.nihongomaster.data.local.dao

import androidx.room.*
import com.example.nihongomaster.data.local.entities.TokenEntity

@Dao
interface TokenDao {
    @Query("SELECT * FROM auth_tokens WHERE id = 1")
    suspend fun getTokens(): TokenEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTokens(token: TokenEntity)

    @Query("DELETE FROM auth_tokens WHERE id = 1")
    suspend fun clearTokens()
}
