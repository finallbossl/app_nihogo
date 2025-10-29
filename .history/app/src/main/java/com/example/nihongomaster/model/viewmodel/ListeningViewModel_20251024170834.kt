package com.example.nihongomaster.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nihongomaster.model.ListeningExercise
import com.example.nihongomaster.model.ListeningQuestion
import com.example.nihongomaster.model.ListeningResult
import kotlinx.coroutines.launch

class ListeningViewModel : ViewModel() {
    
    private val _exercises = MutableLiveData<List<ListeningExercise>>()
    val exercises: LiveData<List<ListeningExercise>> = _exercises

    private val _currentExercise = MutableLiveData<ListeningExercise?>()
    val currentExercise: LiveData<ListeningExercise?> = _currentExercise

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _submitResult = MutableLiveData<Boolean>()
    val submitResult: LiveData<Boolean> = _submitResult

    fun loadExercises() {
        _loading.value = true
        viewModelScope.launch {
            try {
                // TODO: Replace with actual API call
                // val result = listeningRepository.getExercises()
                val mockExercises = getMockExercises()
                _exercises.value = mockExercises
            } catch (e: Exception) {
                _error.value = "Không thể tải danh sách bài nghe"
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadExerciseDetail(exerciseId: String) {
        _loading.value = true
        viewModelScope.launch {
            try {
                // TODO: Replace with actual API call
                // val result = listeningRepository.getExerciseDetail(exerciseId)
                val exercise = getMockExercises().find { it.id == exerciseId }
                _currentExercise.value = exercise
            } catch (e: Exception) {
                _error.value = "Không thể tải chi tiết bài nghe"
            } finally {
                _loading.value = false
            }
        }
    }

    fun submitAnswers(exerciseId: String, answers: Map<String, Int>, timeSpent: Int, listeningCount: Int) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val currentEx = _currentExercise.value
                val score = calculateScore(currentEx?.questions ?: emptyList(), answers)
                
                val result = ListeningResult(
                    exerciseId = exerciseId,
                    answers = answers,
                    timeSpent = timeSpent,
                    score = score,
                    listeningCount = listeningCount
                )

                // TODO: Replace with actual API call
                // listeningRepository.submitResult(result)
                
                _submitResult.value = true
            } catch (e: Exception) {
                _error.value = "Không thể gửi kết quả"
            } finally {
                _loading.value = false
            }
        }
    }

    private fun calculateScore(questions: List<ListeningQuestion>, answers: Map<String, Int>): Int {
        var correct = 0
        questions.forEach { question ->
            val userAnswer = answers[question.id]
            if (userAnswer == question.correctAnswer) {
                correct++
            }
        }
        return (correct * 100) / questions.size
    }

    private fun getMockExercises(): List<ListeningExercise> = listOf(
        ListeningExercise(
            id = "listening-001",
            title = "Tự giới thiệu cơ bản",
            level = "N5",
            category = "Giao tiếp",
            duration = 15,
            difficulty = "Beginner",
            description = "Luyện nghe đoạn hội thoại tự giới thiệu đơn giản",
            audioUrl = "https://example.com/audio/intro-basic.mp3",
            transcript = "はじめまして。わたしは田中です。日本人です。東京に住んでいます。学生です。よろしくお願いします。",
            instructions = "Nghe đoạn audio và trả lời các câu hỏi dưới đây. Bạn có thể nghe lại tối đa 3 lần.",
            questions = listOf(
                ListeningQuestion("l-q1", "Người nói tên gì?", "multiple-choice", 
                    listOf("Sato", "Tanaka", "Yamada", "Suzuki"), 1),
                ListeningQuestion("l-q2", "Người nói sống ở đâu?", "multiple-choice",
                    listOf("Osaka", "Kyoto", "Tokyo", "Nagoya"), 2),
                ListeningQuestion("l-q3", "Nghề nghiệp của người nói là gì?", "multiple-choice",
                    listOf("Nhân viên văn phòng", "Giáo viên", "Học sinh/Sinh viên", "Bác sĩ"), 2)
            )
        ),
        ListeningExercise(
            id = "listening-002",
            title = "Hỏi đường đến ga tàu",
            level = "N4",
            category = "Giao thông",
            duration = 20,
            difficulty = "Intermediate",
            description = "Luyện nghe hội thoại hỏi đường và chỉ đường",
            audioUrl = "https://example.com/audio/directions-station.mp3",
            transcript = "A: すみません、駅はどこですか。B: 駅ですか。まっすぐ行って、交差点を右に曲がってください。A: ありがとうございます。B: どういたしまして。",
            instructions = "Nghe đoạn hội thoại giữa hai người và trả lời các câu hỏi. Chú ý đến hướng dẫn chỉ đường.",
            questions = listOf(
                ListeningQuestion("l-q4", "Người A hỏi gì?", "multiple-choice",
                    listOf("Hỏi giờ", "Hỏi đường đến ga tàu", "Hỏi giá vé", "Hỏi tên đường"), 1),
                ListeningQuestion("l-q5", "Người B hướng dẫn đi như thế nào?", "multiple-choice",
                    listOf("Đi thẳng rồi rẽ trái", "Đi thẳng rồi rẽ phải tại ngã tư", "Quay lại rồi đi thẳng", "Rẽ trái ngay"), 1)
            )
        )
    )

    fun clearError() {
        _error.value = null
    }
}