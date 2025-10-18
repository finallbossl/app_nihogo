package com.example.nihongomaster.model

data class ReadingCategory(
    val id: String,
    val title: String,
    val subtitle: String,
    val progress: Float
)

data class ReadingArticle(
    val id: String,
    val categoryId: String,
    val title: String,
    val jpText: String,
    val highlight: List<String> = emptyList()
)