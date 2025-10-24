package com.example.nihongomaster.model

interface Question {
    val id: String
    val question: String
    val type: String
    val options: List<String>
    val correctAnswer: Int
}