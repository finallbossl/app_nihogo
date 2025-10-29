package com.example.nihongomaster.data.repository

import com.example.nihongomaster.data.remote.ApiClient
import com.example.nihongomaster.data.remote.api.ExerciseResponse
import com.example.nihongomaster.model.TestType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExerciseRepository {
    
    suspend fun getExercises(): Result<List<TestType>> = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.apiService.getExercises()
            if (response.isSuccessful) {
                val exerciseList = response.body()?.map { it.toTestType() } ?: emptyList()
                Result.success(exerciseList)
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun ExerciseResponse.toTestType(): TestType {
        return TestType(
            id = this.id,
            title = this.title,
            subtitle = this.description,
            iconRes = com.example.nihongomaster.R.drawable.ic_test_box
        )
    }
}