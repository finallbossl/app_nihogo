package com.example.nihongomaster.model

data class TestType(
    val id: String,
    val title: String,
    val subtitle: String,
    val iconRes: Int
)

data class TestQuestion(
    val id: String,
    val text: String,
    val options: List<String>,
    val correctAnswer: Int,
    val difficulty: String = "Medium"
)