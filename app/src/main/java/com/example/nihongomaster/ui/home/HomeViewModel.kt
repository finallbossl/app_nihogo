package com.example.nihongomaster.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nihongomaster.R

class HomeViewModel : ViewModel() {
    private val _items = MutableLiveData<List<HomeUiModel>>()
    val items: LiveData<List<HomeUiModel>> = _items

    init {
        // MOCK DATA — có thể thay bằng repository khi có API
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
        add(HomeUiModel.SectionHeader("Start Your Learning Journey"))
        add(HomeUiModel.FeatureCard("vocab", R.drawable.ic_bulb, "Vocabulary Learning",
            "Master Japanese words with interactive flashcards"))
        add(HomeUiModel.FeatureCard("reading", R.drawable.ic_read, "Reading Exercises",
            "Improve your comprehension with engaging articles"))
        add(HomeUiModel.FeatureCard("listening", R.drawable.ic_headphones, "Listening Exercises",
            "Sharpen your skills through audio-based lessons"))
        add(HomeUiModel.FeatureCard("practice", R.drawable.ic_checklist, "Practice Tests",
            "Test your knowledge across all modules and topics"))

        add(HomeUiModel.SectionHeader("Quick Access"))
        add(HomeUiModel.FeatureCard("dashboard", R.drawable.ic_chart, "Progress Dashboard",
            "See streaks, XP and recent activity"))
        add(HomeUiModel.FeatureCard("settings", R.drawable.ic_settings, "User Settings",
            "Manage notifications, goals and preferences"))
    }
}