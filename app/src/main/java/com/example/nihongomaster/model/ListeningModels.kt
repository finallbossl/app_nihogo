package com.example.nihongomaster.model

data class ListeningLesson(
    val id: String,
    val categoryId: String,
    val title: String,
    val level: String,
    val thumbnailRes: Int?,
    val audioDurationSec: Int,
    val audioUrl: String? = null,
    val transcript: String? = null,
    val description: String? = null,
    val questions: List<com.example.nihongomaster.data.remote.api.QuestionResponse>? = null
)

data class ListeningQuestion(
    val id: String,
    val text: String,
    val difficulty: String,
    val options: List<String>,
    val correctIndex: Int
)