package com.example.nihongomaster.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nihongomaster.R
import com.example.nihongomaster.data.repository.VocabularyRepository
import com.example.nihongomaster.model.VocabCategory
import com.example.nihongomaster.model.VocabWord
import kotlinx.coroutines.launch

class VocabularyListViewModel() : ViewModel() {
    
    private val repository = VocabularyRepository()
    
    private val _categories = MutableLiveData<List<VocabCategory>>()
    val categories: LiveData<List<VocabCategory>> = _categories
    
    private val _favoriteWords = MutableLiveData<List<VocabWord>>()
    val favoriteWords: LiveData<List<VocabWord>> = _favoriteWords
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error
    
    init {
        loadVocabularyData()
    }
    
    private fun loadVocabularyData() {
        viewModelScope.launch {
            _isLoading.value = true
            
            repository.getVocabulary()
                .onSuccess { categories ->
                    android.util.Log.d("VocabularyListViewModel", "Loaded ${categories.size} vocabulary categories")
                    _categories.value = categories
                    
                    // Tạm thời để trống favorite words
                    _favoriteWords.value = emptyList()
                }
                .onFailure { exception ->
                    android.util.Log.e("VocabularyListViewModel", "Failed to load vocabulary: ${exception.message}")
                    _error.value = "Không thể kết nối server. Vui lòng kiểm tra kết nối mạng."
                    _categories.value = emptyList()
                    _favoriteWords.value = emptyList()
                }
            
            _isLoading.value = false
        }
    }
    
    fun refreshData() {
        loadVocabularyData()
    }
}