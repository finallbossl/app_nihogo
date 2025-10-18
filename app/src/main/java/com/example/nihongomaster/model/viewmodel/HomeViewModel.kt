package com.example.nihongomaster.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nihongomaster.R
import com.example.nihongomaster.model.HomeUiModel

class HomeViewModel : ViewModel() {
    private val _items = MutableLiveData<List<HomeUiModel>>()
    val items: LiveData<List<HomeUiModel>> = _items

    init {
        _items.value = buildMock()
    }

    private fun buildMock(): List<HomeUiModel> = buildList {
        add(
            HomeUiModel.Welcome(
                title = "Welcome to Nihongo Master!",
                subtitle = "Your personalized path to Japanese fluency starts here. Explore our modules and track your progress.",
                imageRes = R.drawable.img_welcome
            )
        )
        add(HomeUiModel.SectionHeader("🎌 Start Your Japanese Journey"))
        add(
            HomeUiModel.FeatureCard(
                "vocab",
                R.drawable.ic_bulb,
                "📚 Vocabulary Learning",
                "Master Japanese words with interactive flashcards and spaced repetition"
            )
        )
        add(
            HomeUiModel.FeatureCard(
                "reading",
                R.drawable.ic_read,
                "📖 Reading Exercises",
                "Improve comprehension with engaging articles and stories"
            )
        )
        add(
            HomeUiModel.FeatureCard(
                "listening",
                R.drawable.ic_headphones,
                "🎧 Listening Practice",
                "Sharpen your skills through native audio lessons"
            )
        )
        add(
            HomeUiModel.FeatureCard(
                "tests",
                R.drawable.ic_checklist,
                "🏆 Practice Tests",
                "Test your knowledge across all modules and track progress"
            )
        )

        add(HomeUiModel.SectionHeader("📈 Track Your Progress"))
        add(
            HomeUiModel.FeatureCard(
                "progress",
                R.drawable.ic_chart,
                "📈 Progress Dashboard",
                "View detailed stats, streaks, and learning achievements"
            )
        )
    }
}