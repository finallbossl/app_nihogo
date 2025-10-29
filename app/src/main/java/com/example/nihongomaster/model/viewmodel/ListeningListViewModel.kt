package com.example.nihongomaster.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nihongomaster.data.repository.ListeningRepository
import com.example.nihongomaster.model.ListeningLesson
import kotlinx.coroutines.launch

class ListeningListViewModel : ViewModel() {
    private val repository = ListeningRepository()
    
    private val _lessons = MutableLiveData<List<ListeningLesson>>()
    val lessons: LiveData<List<ListeningLesson>> = _lessons
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        loadListeningLessons()
    }
    
    private fun loadListeningLessons() {
        viewModelScope.launch {
            _isLoading.value = true
            
            repository.getListening()
                .onSuccess { lessonList ->
                    android.util.Log.d("ListeningListViewModel", "Loaded ${lessonList.size} listening lessons")
                    _lessons.value = lessonList
                }
                .onFailure { exception ->
                    android.util.Log.e("ListeningListViewModel", "Failed to load listening lessons: ${exception.message}")
                    _error.value = "Không thể kết nối server. Vui lòng kiểm tra kết nối mạng."
                    _lessons.value = emptyList()
                }
            
            _isLoading.value = false
        }
    }
    
    fun refreshData() {
        loadListeningLessons()
    }
}