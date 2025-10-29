package com.example.nihongomaster.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nihongomaster.data.repository.ListeningRepository
import com.example.nihongomaster.model.ListeningLesson
import com.example.nihongomaster.model.ListeningQuestion
import kotlinx.coroutines.launch

class ListeningSessionViewModel : ViewModel() {
    private val repository = ListeningRepository()
    
    private var lessons: List<ListeningLesson> = emptyList()
    private var questions: Map<String, List<ListeningQuestion>> = emptyMap()
    private var index = 0

    private val _current = MutableLiveData<ListeningLesson?>()
    val current: LiveData<ListeningLesson?> = _current

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val selectedAnswers = mutableMapOf<String, Int>()
    
    // Cache for better performance
    private var cachedLessons: List<ListeningLesson>? = null

    fun startWithLesson(lessonId: String) {
        if (_isLoading.value == true) return
        
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                repository.getListeningDetail(lessonId)
                    .onSuccess { lesson ->
                        lessons = listOf(lesson)
                        setupSession()
                    }
                    .onFailure { exception ->
                        _error.value = "Không thể tải bài nghe"
                    }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun start(categoryId: String) {
        if (_isLoading.value == true) return
        
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                // Use cache if available
                val allLessons = cachedLessons ?: run {
                    val result = repository.getListening().getOrNull()
                    cachedLessons = result
                    result
                }
                
                if (allLessons != null) {
                    lessons = if (categoryId.isBlank()) allLessons else allLessons.filter { it.categoryId == categoryId }
                    setupSession()
                } else {
                    _error.value = "Không thể tải danh sách bài nghe"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private fun setupSession() {
        android.util.Log.d("ListeningVM", "Setting up session with ${lessons.size} lessons")
        questions = getQuestionsFromAPI()
        index = 0
        _current.value = lessons.firstOrNull()
        selectedAnswers.clear()
        android.util.Log.d("ListeningVM", "Session setup complete. Current lesson: ${_current.value?.id}")
    }
    

    
    private fun getQuestionsFromAPI(): Map<String, List<ListeningQuestion>> {
        val questionsMap = mutableMapOf<String, List<ListeningQuestion>>()
        
        lessons.forEach { lesson ->
            android.util.Log.d("ListeningVM", "Processing lesson: ${lesson.id}, questions count: ${lesson.questions?.size ?: 0}")
            
            val apiQuestions = lesson.questions
            if (!apiQuestions.isNullOrEmpty()) {
                android.util.Log.d("ListeningVM", "Found ${apiQuestions.size} questions for lesson ${lesson.id}")
                
                val convertedQuestions = apiQuestions.mapIndexedNotNull { index, apiQ ->
                    val questionText = apiQ.text ?: apiQ.question ?: ""
                    val correctIdx = apiQ.correctAnswer ?: apiQ.correctIndex ?: 0
                    
                    android.util.Log.d("ListeningVM", "Question $index: text='$questionText', options=${apiQ.options.size}, correctIdx=$correctIdx")
                    
                    if (questionText.isNotBlank() && apiQ.options.isNotEmpty()) {
                        ListeningQuestion(
                            id = "${lesson.id}_$index",
                            text = questionText,
                            difficulty = apiQ.difficulty ?: "Medium",
                            options = apiQ.options,
                            correctIndex = correctIdx
                        )
                    } else {
                        android.util.Log.w("ListeningVM", "Skipping invalid question: text='$questionText', options=${apiQ.options.size}")
                        null
                    }
                }
                questionsMap[lesson.id] = convertedQuestions
                android.util.Log.d("ListeningVM", "Converted ${convertedQuestions.size} valid questions for lesson ${lesson.id}")
            } else {
                android.util.Log.w("ListeningVM", "No questions found for lesson ${lesson.id}")
                questionsMap[lesson.id] = emptyList()
            }
        }
        
        android.util.Log.d("ListeningVM", "Total questions map size: ${questionsMap.size}")
        return questionsMap
    }
    

    


    fun getQuestionsForCurrent(): List<ListeningQuestion> {
        val id = _current.value?.id ?: return emptyList()
        val questionsForLesson = questions[id].orEmpty()
        android.util.Log.d("ListeningVM", "getQuestionsForCurrent: lessonId=$id, questions=${questionsForLesson.size}")
        return questionsForLesson
    }
    
    // Test method to debug questions
    fun debugQuestions(): String {
        val currentLesson = _current.value
        val questionsForCurrent = getQuestionsForCurrent()
        return buildString {
            append("Current lesson: ${currentLesson?.id}\n")
            append("Total lessons: ${lessons.size}\n")
            append("Questions map size: ${questions.size}\n")
            append("Questions for current: ${questionsForCurrent.size}\n")
            questionsForCurrent.forEachIndexed { index, q ->
                append("Q$index: ${q.text}\n")
            }
        }
    }

    fun selectOption(question: ListeningQuestion, idx: Int) {
        selectedAnswers[question.id] = idx
    }

    fun gradeCurrent(): Pair<Int, Int> {
        val qs = getQuestionsForCurrent()
        if (qs.isEmpty()) return 0 to 0
        var correct = 0
        qs.forEach { q ->
            val chosen = selectedAnswers[q.id]
            if (chosen != null && chosen == q.correctIndex) correct++
        }
        return correct to qs.size
    }


}