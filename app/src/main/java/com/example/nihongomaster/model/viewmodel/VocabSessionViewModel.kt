package com.example.nihongomaster.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nihongomaster.data.repository.VocabularyRepository
import com.example.nihongomaster.model.VocabWord
import kotlinx.coroutines.launch

class VocabSessionViewModel : ViewModel() {
    private val repository = VocabularyRepository()
    private lateinit var words: List<VocabWord>
    private var index = 0
    
    private val _current = MutableLiveData<VocabWord>()
    val current: LiveData<VocabWord> = _current

    private val _revealed = MutableLiveData(false)
    val revealed: LiveData<Boolean> = _revealed

    private val _learned = MutableLiveData(0)
    val learned: LiveData<Int> = _learned
    private val _due = MutableLiveData(0)
    val due: LiveData<Int> = _due
    private val _total = MutableLiveData(0)
    val total: LiveData<Int> = _total
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error
    
    fun currentWordId(): String? {
        return _current.value?.id
    }
    
    fun start(categoryId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            
            repository.getVocabWords(categoryId)
                .onSuccess { wordList ->
                    android.util.Log.d("VocabSessionViewModel", "Loaded ${wordList.size} words for category $categoryId")
                    
                    if (wordList.isNotEmpty()) {
                        words = wordList
                        index = 0
                        _total.value = words.size
                        _due.value = words.size
                        _learned.value = 0
                        _revealed.value = false
                        _current.value = words.first()
                    } else {
                        _error.value = "Không có từ vựng nào trong danh mục này"
                    }
                }
                .onFailure { exception ->
                    android.util.Log.e("VocabSessionViewModel", "Failed to load words: ${exception.message}")
                    _error.value = "Không thể tải từ vựng: ${exception.message}"
                }
            
            _isLoading.value = false
        }
    }

    fun reveal() { _revealed.value = true }

    private fun next() {
        if (::words.isInitialized && words.isNotEmpty()) {
            index = (index + 1).coerceAtMost(words.lastIndex)
            _current.value = words[index]
            _revealed.value = false
        }
    }

    fun markKnown() {
        _learned.value = (_learned.value ?: 0) + 1
        _due.value = (_due.value ?: 0) - 1
        if (::words.isInitialized && index < words.lastIndex) next()
    }

    fun markUnknown() {
        _due.value = (_due.value ?: 0)
        if (::words.isInitialized && index < words.lastIndex) next()
    }

    fun skip() { 
        if (::words.isInitialized && index < words.lastIndex) next() 
    }
}