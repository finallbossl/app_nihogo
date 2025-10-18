package com.example.nihongomaster.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nihongomaster.model.VocabWord

class VocabSessionViewModel : ViewModel() {
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

    fun currentWordId(): String? {
        return _current.value?.id
    }

    fun start(categoryId: String) {
        words = when (categoryId) {
            "basic" -> listOf(
                VocabWord("basic_1", "こんにちは", "konnichiwa", "hello", "N5"),
                VocabWord("basic_2", "ありがとう", "arigatou", "thank you", "N5"),
                VocabWord("basic_3", "さようなら", "sayounara", "goodbye", "N5"),
            )

            "travel" -> listOf(
                VocabWord("travel_1", "駅", "eki", "station", "N5"),
                VocabWord("travel_2", "地図", "chizu", "map", "N5"),
                VocabWord("travel_3", "切符", "kippu", "ticket", "N5"),
            )

            else -> listOf(
                VocabWord("misc_1", "時間", "jikan", "time", "N5"),
                VocabWord("misc_2", "友達", "tomodachi", "friend", "N5"),
                VocabWord("misc_3", "勉強", "benkyou", "study", "N5"),
            )
        }
        index = 0
        _total.value = words.size
        _due.value = words.size
        _learned.value = 0
        _revealed.value = false
        _current.value = words.first()
    }

    fun reveal() {
        _revealed.value = true
    }

    private fun next() {
        index = (index + 1).coerceAtMost(words.lastIndex)
        _current.value = words[index]
        _revealed.value = false
    }

    fun markKnown() {
        _learned.value = (_learned.value ?: 0) + 1
        _due.value = (_due.value ?: 0) - 1
        if (index < words.lastIndex) next()
    }

    fun markUnknown() {
        _due.value = (_due.value ?: 0)
        if (index < words.lastIndex) next()
    }

    fun skip() {
        if (index < words.lastIndex) next()
    }
}