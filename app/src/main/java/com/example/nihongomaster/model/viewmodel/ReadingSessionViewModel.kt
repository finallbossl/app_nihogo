package com.example.nihongomaster.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nihongomaster.data.repository.ReadingRepository
import com.example.nihongomaster.model.ReadingArticle
import kotlinx.coroutines.launch

class ReadingSessionViewModel : ViewModel() {
    private val repository = ReadingRepository()
    private lateinit var list: List<ReadingArticle>
    private var index = 0

    private val _current = MutableLiveData<ReadingArticle?>()
    val current: LiveData<ReadingArticle?> = _current

    private val _total = MutableLiveData(0)
    val total: LiveData<Int> = _total
    private val _pos = MutableLiveData(1)
    val pos: LiveData<Int> = _pos
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun start(categoryId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            
            repository.getReadingArticles(categoryId)
                .onSuccess { articles ->
                    android.util.Log.d("ReadingSessionViewModel", "Loaded ${articles.size} articles for category $categoryId")
                    
                    if (articles.isNotEmpty()) {
                        list = articles
                        _total.value = list.size
                        index = 0
                        _pos.value = 1
                        _current.value = list.firstOrNull()
                    } else {
                        _error.value = "Không có bài đọc nào trong danh mục này"
                    }
                }
                .onFailure { exception ->
                    android.util.Log.e("ReadingSessionViewModel", "Failed to load articles: ${exception.message}")
                    _error.value = "Không thể tải bài đọc: ${exception.message}"
                }
            
            _isLoading.value = false
        }
    }

    fun previous() {
        if (::list.isInitialized && index > 0) {
            index--
            _pos.value = index + 1
            _current.value = list[index]
        }
    }

    fun next() {
        if (::list.isInitialized && index < list.lastIndex) {
            index++
            _pos.value = index + 1
            _current.value = list[index]
        }
    }

    fun currentArticleId(): String? = _current.value?.id
}