package com.example.nihongomaster.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nihongomaster.data.repository.ReadingRepository
import com.example.nihongomaster.model.ReadingCategory
import kotlinx.coroutines.launch

class ReadingListViewModel : ViewModel() {
    private val repository = ReadingRepository()
    
    private val _categories = MutableLiveData<List<ReadingCategory>>()
    val categories: LiveData<List<ReadingCategory>> = _categories
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        loadReadingCategories()
    }
    
    private fun loadReadingCategories() {
        viewModelScope.launch {
            _isLoading.value = true
            
            repository.getReadingCategories()
                .onSuccess { categoryList ->
                    android.util.Log.d("ReadingListViewModel", "Loaded ${categoryList.size} reading categories")
                    _categories.value = categoryList
                }
                .onFailure { exception ->
                    android.util.Log.e("ReadingListViewModel", "Failed to load reading categories: ${exception.message}")
                    _error.value = "Không thể kết nối server. Vui lòng kiểm tra kết nối mạng."
                    _categories.value = emptyList()
                }
            
            _isLoading.value = false
        }
    }
    
    fun refreshData() {
        loadReadingCategories()
    }
}