package com.example.nihongomaster.model

data class ListeningExercise(
    val id: String,
    val title: String,
    val audioUrl: String? = null,
    val transcript: String? = null,
    val level: String,
    val category: String,
    val duration: Int,
    val difficulty: String,
    val thumbnail: String? = null,
    val description: String,
    val instructions: String? = null,
    val questions: List<ListeningQuestion> = emptyList(),
    val isCompleted: Boolean = false
)

data class ListeningQuestion(
    override val id: String,
    override val question: String,
    override val type: String,
    override val options: List<String>,
    override val correctAnswer: Int
) : Question

data class ListeningResult(
    val id: String? = null,
    val exerciseId: String,
    val answers: Map<String, Int>,
    val timeSpent: Int = 0,
    val score: Int = 0,
    val listeningCount: Int = 1,
    val completedAt: String? = null
)