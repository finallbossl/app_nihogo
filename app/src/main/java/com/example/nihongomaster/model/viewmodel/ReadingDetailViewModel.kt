package com.example.nihongomaster.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class ReadingDetail(
    val id: String,
    val title: String,
    val jpText: String,
    val romajiNotes: String,
    val vocabNotes: List<Pair<String, String>>
)

class ReadingDetailViewModel : ViewModel() {
    private val _detail = MutableLiveData<ReadingDetail>()
    val detail: LiveData<ReadingDetail> = _detail

    fun load(articleId: String) {
        _detail.value = when (articleId) {
            "inter_1" -> ReadingDetail(
                "inter_1",
                "中級読解演習",
                "今日の東京の天気は晴れで、最高気温は25度です。週末は桜が満開になり、多くの人々が花見に出かけるでしょう。",
                "tenki (weather), saikō kion (highest temperature), hanami (flower viewing)",
                listOf(
                    "天気" to "weather",
                    "最高気温" to "highest temperature",
                    "花見" to "cherry-blossom viewing"
                )
            )

            else -> ReadingDetail(
                articleId, "自己紹介",
                "はじめまして。わたしはアキです。ベトナムから来ました。よろしくお願いします。",
                "hajimemashite, yoroshiku onegai shimasu",
                listOf(
                    "はじめまして" to "Nice to meet you",
                    "よろしくお願いします" to "Please treat me favorably"
                )
            )
        }
    }
}