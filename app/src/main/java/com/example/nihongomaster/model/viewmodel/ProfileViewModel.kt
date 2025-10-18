package com.example.nihongomaster.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nihongomaster.R
import com.example.nihongomaster.model.ActivityItem
import com.example.nihongomaster.model.Achievement

class ProfileViewModel : ViewModel() {

    private val _name = MutableLiveData("Nguyen Minh")
    val name: LiveData<String> = _name

    private val _level = MutableLiveData("Level: N5 Beginner")
    val level: LiveData<String> = _level

    private val _streak = MutableLiveData(7)
    val streak: LiveData<Int> = _streak

    private val _words = MutableLiveData(185)
    val words: LiveData<Int> = _words

    private val _accuracy = MutableLiveData(87)
    val accuracy: LiveData<Int> = _accuracy

    private val _summary =
        MutableLiveData("Focus Vocabulary • 3 lessons/week • Goal: N5 in 3 months.")
    val summary: LiveData<String> = _summary

    private val _summaryProgress = MutableLiveData(65)
    val summaryProgress: LiveData<Int> = _summaryProgress

    private val _activities = MutableLiveData(
        listOf(
            ActivityItem(
                "pa1", R.drawable.ic_reading_box, "Completed N5 Reading - Lesson 3",
                "Daily expressions for greetings.", "2 hours ago", null
            ),
            ActivityItem(
                "pa2", R.drawable.ic_vocab_box, "Mastered 10 New Words",
                "New set of everyday vocabulary.", "Yesterday", "Vocabulary"
            ),
            ActivityItem(
                "pa3", R.drawable.ic_listening_box, "Listening: Daily Conversation",
                "Scored 85% on dialogue comprehension.", "3 days ago", null
            )
        )
    )
    val activities: LiveData<List<ActivityItem>> = _activities

    private val _achievements = MutableLiveData(
        listOf(
            Achievement("pg1", R.drawable.ic_bolt, "First 100 Words", "Vocabulary Learner", true),
            Achievement("pg2", R.drawable.ic_streak, "7-Day Streak", "Keep it up!", true),
            Achievement(
                "pg3",
                R.drawable.ic_medal,
                "N5 Completion",
                "Core lessons finished",
                false
            ),
            Achievement("pg4", R.drawable.ic_ear, "Listening Ace", "10 audio sessions", false),
            Achievement("pg5", R.drawable.ic_kana, "Kana Master", "Hiragana + Katakana", true),
            Achievement("pg6", R.drawable.ic_trophy, "Grammar Guru", "15 grammar units", false),
        )
    )
    val achievements: LiveData<List<Achievement>> = _achievements

    fun updateName(newName: String) {
        _name.value = newName
    }
}