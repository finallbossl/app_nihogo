package com.example.nihongomaster.ui.vocab


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nihongomaster.R

class VocabularyListViewModel : ViewModel() {
    private val _categories = MutableLiveData<List<VocabCategory>>()
    val categories: LiveData<List<VocabCategory>> = _categories

    init {
        _categories.value = listOf(
            VocabCategory("basic", R.drawable.ic_book_open, "Basic Vocabulary",
                "Essential words for everyday conversations and fundamental understanding.",
                0.35f),
            VocabCategory("kanji", R.drawable.ic_t_kanji, "Kanji Basics",
                "Learn fundamental Kanji characters and their readings.", 0.22f),
            VocabCategory("daily", R.drawable.ic_chat, "Daily Conversation",
                "Phrases and expressions for common social interactions.", 0.48f),
            VocabCategory("travel", R.drawable.ic_grid, "Travel Phrases",
                "Useful vocabulary for navigating and communicating while traveling in Japan.",
                0.62f),
            VocabCategory("business", R.drawable.ic_badge, "Business Japanese",
                "Key terminology and polite expressions for professional settings.",
                0.18f)
        )
    }
}