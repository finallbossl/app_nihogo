package com.example.nihongomaster.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nihongomaster.model.ReadingCategory

class ReadingListViewModel : ViewModel() {
    private val _cats = MutableLiveData<List<ReadingCategory>>()
    val categories: LiveData<List<ReadingCategory>> = _cats

    init {
        _cats.value = listOf(
            ReadingCategory("beginner", "📚 Beginner Stories", "Simple daily life stories", 0.65f),
            ReadingCategory("intermediate", "📰 News Articles", "Current events and topics", 0.35f),
            ReadingCategory(
                "culture",
                "🏯 Japanese Culture",
                "Traditional and modern culture",
                0.20f
            ),
            ReadingCategory("travel", "🗾 Travel Guide", "Places and travel experiences", 0.40f),
            ReadingCategory("food", "🍱 Food & Recipes", "Japanese cuisine and cooking", 0.55f),
            ReadingCategory("business", "💼 Business Japanese", "Workplace communication", 0.10f)
        )
    }
}