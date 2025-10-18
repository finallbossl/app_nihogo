package com.example.nihongomaster.ui.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nihongomaster.R
import com.example.nihongomaster.databinding.ItemNotificationBinding
import com.example.nihongomaster.databinding.ItemNotificationSectionBinding
import com.example.nihongomaster.model.NotificationItem
import com.example.nihongomaster.model.NotificationType

class NotificationAdapter(
    private val onNotificationClick: (NotificationItem) -> Unit
) : ListAdapter<NotificationUiModel, RecyclerView.ViewHolder>(Diff) {

    object Diff : DiffUtil.ItemCallback<NotificationUiModel>() {
        override fun areItemsTheSame(
            oldItem: NotificationUiModel,
            newItem: NotificationUiModel
        ): Boolean =
            when {
                oldItem is NotificationUiModel.Section && newItem is NotificationUiModel.Section ->
                    oldItem.title == newItem.title

                oldItem is NotificationUiModel.Item && newItem is NotificationUiModel.Item ->
                    oldItem.notification.id == newItem.notification.id

                else -> false
            }

        override fun areContentsTheSame(
            oldItem: NotificationUiModel,
            newItem: NotificationUiModel
        ): Boolean =
            oldItem == newItem
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is NotificationUiModel.Section -> 0
        is NotificationUiModel.Item -> 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            0 -> SectionVH(ItemNotificationSectionBinding.inflate(inflater, parent, false))
            else -> ItemVH(
                ItemNotificationBinding.inflate(inflater, parent, false),
                onNotificationClick
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is NotificationUiModel.Section -> (holder as SectionVH).bind(item)
            is NotificationUiModel.Item -> (holder as ItemVH).bind(item.notification)
        }
    }

    class SectionVH(private val binding: ItemNotificationSectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(section: NotificationUiModel.Section) {
            binding.tvSection.text = section.title
        }
    }

    class ItemVH(
        private val binding: ItemNotificationBinding,
        private val onClick: (NotificationItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: NotificationItem) {
            binding.apply {
                tvTitle.text = notification.title
                tvMessage.text = notification.message
                tvTime.text = notification.time

                val iconRes = when (notification.type) {
                    NotificationType.STREAK_REMINDER -> R.drawable.ic_streak
                    NotificationType.LESSON_COMPLETE -> R.drawable.ic_checklist
                    NotificationType.ACHIEVEMENT -> R.drawable.ic_trophy
                    NotificationType.DAILY_GOAL -> R.drawable.ic_bolt
                    NotificationType.NEW_CONTENT -> R.drawable.ic_bulb
                    NotificationType.SYSTEM -> R.drawable.ic_settings
                }
                ivIcon.setImageResource(iconRes)

                root.alpha = if (notification.isRead) 0.7f else 1.0f
                root.setOnClickListener { onClick(notification) }
            }
        }
    }
}

sealed class NotificationUiModel {
    data class Section(val title: String) : NotificationUiModel()
    data class Item(val notification: NotificationItem) : NotificationUiModel()
}