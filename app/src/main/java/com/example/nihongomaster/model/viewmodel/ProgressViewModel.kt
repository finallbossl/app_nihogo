package com.example.nihongomaster.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nihongomaster.R
import com.example.nihongomaster.model.*

class ProgressViewModel : ViewModel() {

    private val _metrics = MutableLiveData(ProgressMetrics(15, 342, 87, 28, 2700))
    val metrics: LiveData<ProgressMetrics> = _metrics

    private val _studyModules = MutableLiveData(
        listOf(
            StudyModule("vocab", "Vocabulary N5", R.drawable.ic_vocab_box, 68, 50, 34),
            StudyModule("grammar", "Grammar Basics", R.drawable.ic_edit_box, 45, 20, 9),
            StudyModule("kanji", "Kanji N5", R.drawable.ic_t_kanji, 30, 100, 30),
            StudyModule("listening", "Listening Practice", R.drawable.ic_listening_box, 55, 25, 14)
        )
    )
    val studyModules: LiveData<List<StudyModule>> = _studyModules

    private val _ongoingLessons = MutableLiveData(
        listOf(
            OngoingLesson("l1", "Daily Conversation", "Listening", 75, R.drawable.ic_listening_box),
            OngoingLesson("l2", "Family & Friends", "Vocabulary", 40, R.drawable.ic_vocab_box),
            OngoingLesson("l3", "Basic Grammar", "Grammar", 90, R.drawable.ic_edit_box)
        )
    )
    val ongoingLessons: LiveData<List<OngoingLesson>> = _ongoingLessons

    private val _favoriteWords = MutableLiveData<List<FavoriteWord>>()
    val favoriteWords: LiveData<List<FavoriteWord>> = _favoriteWords
    
    init {
        loadFavoriteWords()
    }
    
    private fun loadFavoriteWords() {
        val vocabWords = FavoriteManager.getFavoriteWords()
        val favoriteWords = vocabWords.map { 
            FavoriteWord(it.id, it.kanji, it.hiragana, it.meaning, it.level)
        }
        _favoriteWords.value = favoriteWords
    }
}