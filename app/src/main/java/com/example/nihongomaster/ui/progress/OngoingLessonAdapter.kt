package com.example.nihongomaster.ui.progress

import com.example.nihongomaster.model.*

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nihongomaster.databinding.ItemOngoingLessonBinding

class OngoingLessonAdapter : ListAdapter<OngoingLesson, OngoingLessonAdapter.ViewHolder>(Diff) {

    object Diff : DiffUtil.ItemCallback<OngoingLesson>() {
        override fun areItemsTheSame(oldItem: OngoingLesson, newItem: OngoingLesson): Boolean =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: OngoingLesson, newItem: OngoingLesson): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemOngoingLessonBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemOngoingLessonBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lesson: OngoingLesson) {
            binding.apply {
                ivIcon.setImageResource(lesson.iconRes)
                tvTitle.text = lesson.title
                tvType.text = lesson.type
                progressBar.progress = lesson.progress
                tvProgress.text = "${lesson.progress}%"
            }
        }
    }
}