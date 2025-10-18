package com.example.nihongomaster.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nihongomaster.ui.notifications.NotificationUiModel

class NotificationsViewModel : ViewModel() {
    private val _notifications = MutableLiveData<List<NotificationUiModel>>()
    val notifications: LiveData<List<NotificationUiModel>> = _notifications

    fun markAsRead(id: String) {}
    fun clearAll() {}
}