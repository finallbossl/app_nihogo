package com.example.nihongomaster.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nihongomaster.model.VocabWord

data class ExampleSentence(val jp: String, val en: String)
data class RelatedWord(val surface: String, val gloss: String, val link: String?)

data class VocabDetail(
    val wordId: String,
    val headwordJp: String,
    val romaji: String,
    val ipa: String,
    val definitionShort: String,
    val definitionLong: String,
    val examples: List<ExampleSentence>,
    val related: List<RelatedWord>
)

class VocabDetailViewModel : ViewModel() {
    private val _detail = MutableLiveData<VocabDetail>()
    val detail: LiveData<VocabDetail> = _detail

    private val _isFavorite = MutableLiveData<Boolean>(false)
    val isFavorite: LiveData<Boolean> = _isFavorite

    private var currentWordId: String = ""

    fun load(wordId: String) {
        val d = when (wordId) {
            "basic_2" -> VocabDetail(
                wordId = wordId,
                headwordJp = "ありがとう",
                romaji = "arigatou",
                ipa = "[a.ɾi.ga.toː]",
                definitionShort = "A common Japanese expression of gratitude, meaning thank you.",
                definitionLong = "A versatile expression used in various social contexts; formality can be raised with phrases like gozaimasu.",
                examples = listOf(
                    ExampleSentence("ありがとう。", "Thank you."),
                    ExampleSentence("手伝ってくれてありがとう。", "Thank you for helping me."),
                    ExampleSentence("いつもありがとう！", "Thanks as always!")
                ),
                related = listOf(
                    RelatedWord("どうも", "Thanks (casual), very", null),
                    RelatedWord("ありがとうございます", "Thank you (polite)", null),
                    RelatedWord("すみません", "Excuse me / sorry; thank you", null)
                )
            )

            else -> VocabDetail(
                wordId = wordId,
                headwordJp = "こんにちは",
                romaji = "konnichiwa",
                ipa = "[koɲ.ni.t͡ɕi.wa]",
                definitionShort = "A general daytime greeting similar to hello.",
                definitionLong = "Common from late morning to sunset; more formal alternatives exist depending on context.",
                examples = listOf(
                    ExampleSentence("こんにちは。", "Hello / Good afternoon."),
                    ExampleSentence("こんにちは、元気？", "Hi, how are you?")
                ),
                related = listOf(
                    RelatedWord("おはよう", "Good morning", null),
                    RelatedWord("こんばんは", "Good evening", null)
                )
            )
        }
        _detail.value = d
        currentWordId = wordId
        _isFavorite.value = FavoriteManager.isFavorite(wordId)
    }

    fun toggleFavorite() {
        val current = _isFavorite.value ?: false
        _isFavorite.value = !current

        if (!current) {
            FavoriteManager.addToFavorites(currentWordId)
        } else {
            FavoriteManager.removeFromFavorites(currentWordId)
        }
    }
}

object FavoriteManager {
    private val favorites = mutableSetOf<String>()

    fun addToFavorites(wordId: String) {
        favorites.add(wordId)
    }

    fun removeFromFavorites(wordId: String) {
        favorites.remove(wordId)
    }

    fun isFavorite(wordId: String): Boolean {
        return favorites.contains(wordId)
    }

    fun getFavoriteWords(): List<VocabWord> {
        val allWords = listOf(
            VocabWord("basic_1", "こんにちは", "konnichiwa", "hello", "N5", true),
            VocabWord("basic_2", "ありがとう", "arigatou", "thank you", "N5", true),
            VocabWord("fav1", "学校", "がっこう", "school", "N5", true),
            VocabWord("fav2", "友達", "ともだち", "friend", "N5", true),
            VocabWord("fav3", "家族", "かぞく", "family", "N5", true)
        )
        return allWords.filter { favorites.contains(it.id) }
    }
}