package com.example.nihongomaster.ui.home

sealed class HomeUiModel {
    data class Welcome(
        val title: String,
        val subtitle: String,
        val imageRes: Int
    ) : HomeUiModel()

    data class SectionHeader(val title: String) : HomeUiModel()

    data class FeatureCard(
        val id: String,
        val iconRes: Int,
        val title: String,
        val subtitle: String
    ) : HomeUiModel()
}