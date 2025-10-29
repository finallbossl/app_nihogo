package com.example.nihongomaster.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nihongomaster.model.TestQuestion

class TestSessionViewModel : ViewModel() {
    private val _current = MutableLiveData<TestQuestion?>()
    val current: LiveData<TestQuestion?> = _current
    
    private val _position = MutableLiveData<Int>()
    val position: LiveData<Int> = _position
    
    private val _total = MutableLiveData<Int>()
    val total: LiveData<Int> = _total
    
    private var questions = listOf<TestQuestion>()
    private var currentIndex = 0
    private var selectedAnswer = -1
    private val answers = mutableMapOf<String, Int>()

    fun start(typeId: String) {
        questions = when (typeId) {
            "vocab_quiz" -> listOf(
                TestQuestion("q1", "What does 'こんにちは' mean?", listOf("Hello", "Goodbye", "Thank you", "Excuse me"), 0),
                TestQuestion("q2", "How do you say 'thank you' in Japanese?", listOf("こんにちは", "ありがとう", "すみません", "さようなら"), 1),
                TestQuestion("q3", "What is the meaning of '学校'?", listOf("Home", "School", "Hospital", "Store"), 1)
            )
            else -> listOf(
                TestQuestion("q1", "Choose the correct particle: 私___学生です", listOf("は", "が", "を", "に"), 0),
                TestQuestion("q2", "What is the polite form of 'する'?", listOf("します", "した", "して", "しよう"), 0)
            )
        }
        currentIndex = 0
        selectedAnswer = -1
        answers.clear()
        _total.value = questions.size
        _position.value = 1
        _current.value = questions.firstOrNull()
    }
    
    fun next(): Boolean {
        if (currentIndex < questions.size - 1) {
            currentIndex++
            selectedAnswer = -1
            _position.value = currentIndex + 1
            _current.value = questions[currentIndex]
            return true
        }
        return false
    }
    
    fun hasChosen(): Boolean = selectedAnswer != -1
    
    fun choose(idx: Int) {
        selectedAnswer = idx
        _current.value?.let { question ->
            answers[question.id] = idx
        }
    }
    
    fun grade(): Pair<Int, Int> {
        var correct = 0
        questions.forEach { question ->
            if (answers[question.id] == question.correctAnswer) {
                correct++
            }
        }
        return correct to questions.size
    }
}