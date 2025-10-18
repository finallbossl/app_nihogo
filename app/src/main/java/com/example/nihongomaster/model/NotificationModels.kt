package com.example.nihongomaster.model

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val time: String,
    val type: NotificationType,
    val isRead: Boolean = false
)

enum class NotificationType {
    STREAK_REMINDER,
    LESSON_COMPLETE,
    ACHIEVEMENT,
    DAILY_GOAL,
    NEW_CONTENT,
    SYSTEM
}