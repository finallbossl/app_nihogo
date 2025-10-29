package com.example.nihongomaster.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nihongomaster.R
import com.example.nihongomaster.model.TestType

class TestListViewModel : ViewModel() {
    private val _items = MutableLiveData<List<TestType>>()
    val items: LiveData<List<TestType>> = _items
    
    init {
        loadMockData()
    }
    
    private fun loadMockData() {
        _items.value = listOf(
            TestType("vocab_quiz", "📝 Vocabulary Quiz", "Test your N5 vocabulary knowledge", R.drawable.ic_vocab_box),
            TestType("grammar_test", "📚 Grammar Test", "Practice basic Japanese grammar", R.drawable.ic_edit_box),
            TestType("kanji_quiz", "日 Kanji Recognition", "Identify common kanji characters", R.drawable.ic_t_kanji),
            TestType("listening_test", "🎧 Listening Comprehension", "Audio-based understanding test", R.drawable.ic_listening_box),
            TestType("reading_test", "📰 Reading Comprehension", "Short passage reading test", R.drawable.ic_reading_box),
            TestType("mixed_test", "🎯 Mixed Practice", "Combined skills assessment", R.drawable.ic_trophy)
        )
    }
}