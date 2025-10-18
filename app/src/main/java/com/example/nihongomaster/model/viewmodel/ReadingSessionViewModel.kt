package com.example.nihongomaster.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nihongomaster.model.ReadingArticle

class ReadingSessionViewModel : ViewModel() {
    private lateinit var list: List<ReadingArticle>
    private var index = 0

    private val _current = MutableLiveData<ReadingArticle?>()
    val current: LiveData<ReadingArticle?> = _current

    private val _total = MutableLiveData(0)
    val total: LiveData<Int> = _total
    private val _pos = MutableLiveData(1)
    val pos: LiveData<Int> = _pos

    fun start(categoryId: String) {
        list = mock(categoryId)
        _total.value = list.size
        index = 0
        _pos.value = 1
        _current.value = list.firstOrNull()
    }

    fun previous() {
        if (index > 0) {
            index--
            _pos.value = index + 1
            _current.value = list[index]
        }
    }

    fun next() {
        if (index < list.lastIndex) {
            index++
            _pos.value = index + 1
            _current.value = list[index]
        }
    }

    fun currentArticleId(): String? = _current.value?.id

    private fun mock(cat: String): List<ReadingArticle> = when (cat) {
        "intermediate" -> listOf(
            ReadingArticle(
                "inter_1", "intermediate",
                "中級読解演習 (Intermediate Reading Exercise)",
                "今日の東京の天気は晴れで、最高気温は25度です。週末は桜が満開になり、多くの人々が花見に出かけるでしょう。日本の文化に触れる良い機会です。",
                highlight = listOf("天気", "最高気温", "花見")
            ),
            ReadingArticle(
                "inter_2", "intermediate",
                "日本の朝ごはん",
                "日本の朝ごはんは、ご飯、味噌汁、焼き魚、漬物などが一般的です。忙しい人はパンやコーヒーだけのこともあります。"
            )
        )

        else -> listOf(
            ReadingArticle(
                "beg_1", "beginner", "自己紹介",
                "はじめまして。わたしはアキです。ベトナムから来ました。よろしくお願いします。"
            ),
            ReadingArticle(
                "beg_2", "beginner", "買い物",
                "スーパーで野菜と果物を買いました。りんごは安くて、とても新鮮でした。"
            )
        )
    }
}