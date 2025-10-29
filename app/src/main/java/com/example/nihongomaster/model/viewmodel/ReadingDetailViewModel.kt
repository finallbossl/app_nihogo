package com.example.nihongomaster.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nihongomaster.data.repository.ReadingRepository
import kotlinx.coroutines.launch

data class ReadingDetail(
    val id: String,
    val title: String,
    val jpText: String,
    val romajiNotes: String,
    val vocabNotes: List<Pair<String,String>>
)

class ReadingDetailViewModel : ViewModel() {
    private val repository = ReadingRepository()
    
    private val _detail = MutableLiveData<ReadingDetail>()
    val detail: LiveData<ReadingDetail> = _detail
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun load(categoryId: String, articleId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            
            repository.getReadingArticles(categoryId)
                .onSuccess { articles ->
                    val article = articles.find { it.id == articleId }
                    if (article != null) {
                        _detail.value = ReadingDetail(
                            id = article.id,
                            title = article.title,
                            jpText = article.jpText,
                            romajiNotes = "", // Tạm thời để trống
                            vocabNotes = emptyList() // Tạm thời để trống
                        )
                    } else {
                        _error.value = "Không tìm thấy bài đọc"
                    }
                }
                .onFailure { exception ->
                    android.util.Log.e("ReadingDetailViewModel", "Failed to load article: ${exception.message}")
                    _error.value = "Không thể tải bài đọc: ${exception.message}"
                }
            
            _isLoading.value = false
        }
    }
}