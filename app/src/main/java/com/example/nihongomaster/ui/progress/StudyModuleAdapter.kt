package com.example.nihongomaster.ui.progress

import com.example.nihongomaster.model.*

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nihongomaster.databinding.ItemStudyModuleBinding

class StudyModuleAdapter : ListAdapter<StudyModule, StudyModuleAdapter.ViewHolder>(Diff) {

    object Diff : DiffUtil.ItemCallback<StudyModule>() {
        override fun areItemsTheSame(oldItem: StudyModule, newItem: StudyModule): Boolean =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: StudyModule, newItem: StudyModule): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemStudyModuleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemStudyModuleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(module: StudyModule) {
            binding.apply {
                ivIcon.setImageResource(module.iconRes)
                tvModuleName.text = module.name
                tvProgress.text = "${module.completedItems}/${module.totalItems}"
                progressBar.progress = module.progress
            }
        }
    }
}