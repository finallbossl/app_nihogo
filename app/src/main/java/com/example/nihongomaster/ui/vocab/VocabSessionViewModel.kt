package com.example.nihongomaster.ui.vocab

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class VocabWord(val jp: String, val romaji: String, val meaning: String)

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

    fun start(categoryId: String) {
        // mock list theo category
        words = when (categoryId) {
            "basic" -> listOf(
                VocabWord("こんにちは", "konnichiwa", "hello"),
                VocabWord("ありがとう", "arigatou", "thank you"),
                VocabWord("さようなら", "sayounara", "goodbye"),
            )
            "travel" -> listOf(
                VocabWord("駅", "eki", "station"),
                VocabWord("地図", "chizu", "map"),
                VocabWord("切符", "kippu", "ticket"),
            )
            else -> listOf(
                VocabWord("時間", "jikan", "time"),
                VocabWord("友達", "tomodachi", "friend"),
                VocabWord("勉強", "benkyou", "study"),
            )
        }
        index = 0
        _total.value = words.size
        _due.value = words.size
        _learned.value = 0
        _revealed.value = false
        _current.value = words.first()
    }

    fun reveal() { _revealed.value = true }

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
        _due.value = (_due.value ?: 0) // giữ nguyên, có thể xếp lại hàng
        if (index < words.lastIndex) next()
    }

    fun skip() { if (index < words.lastIndex) next() }
}