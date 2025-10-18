package com.example.nihongomaster.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nihongomaster.R
import com.example.nihongomaster.model.VocabCategory
import com.example.nihongomaster.model.VocabWord

class VocabularyListViewModel : ViewModel() {
    
    private val _categories = MutableLiveData<List<VocabCategory>>()
    val categories: LiveData<List<VocabCategory>> = _categories
    
    private val _favoriteWords = MutableLiveData<List<VocabWord>>()
    val favoriteWords: LiveData<List<VocabWord>> = _favoriteWords
    
    init {
        loadMockData()
    }
    
    private fun loadMockData() {
        _categories.value = listOf(
            VocabCategory("basic", R.drawable.ic_vocab_box, "🌟 Basic Greetings", "Essential daily greetings", 0.8f),
            VocabCategory("family", R.drawable.ic_vocab_box, "👨👩👧👦 Family & People", "Family members and relationships", 0.6f),
            VocabCategory("food", R.drawable.ic_vocab_box, "🍜 Food & Drinks", "Common foods and beverages", 0.4f),
            VocabCategory("travel", R.drawable.ic_vocab_box, "✈️ Travel & Transport", "Transportation and travel terms", 0.3f),
            VocabCategory("time", R.drawable.ic_vocab_box, "⏰ Time & Numbers", "Time expressions and numbers", 0.7f),
            VocabCategory("school", R.drawable.ic_vocab_box, "📚 School & Work", "Education and workplace vocabulary", 0.2f)
        )
        
        _favoriteWords.value = listOf(
            VocabWord("fav1", "こんにちは", "konnichiwa", "hello", "N5", true),
            VocabWord("fav2", "ありがとう", "arigatou", "thank you", "N5", true),
            VocabWord("fav3", "学校", "gakkou", "school", "N5", true),
            VocabWord("fav4", "友達", "tomodachi", "friend", "N5", true),
            VocabWord("fav5", "家族", "kazoku", "family", "N5", true)
        )
    }
}