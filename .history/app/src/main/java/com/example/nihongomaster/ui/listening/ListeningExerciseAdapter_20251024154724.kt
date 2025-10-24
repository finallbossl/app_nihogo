package com.example.nihongomaster.ui.listening

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nihongomaster.databinding.ItemListeningExerciseBinding
import com.example.nihongomaster.model.ListeningExercise

class ListeningExerciseAdapter(
    private val onExerciseClick: (ListeningExercise) -> Unit
) : ListAdapter<ListeningExercise, ListeningExerciseAdapter.ViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<ListeningExercise>() {
        override fun areItemsTheSame(oldItem: ListeningExercise, newItem: ListeningExercise): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ListeningExercise, newItem: ListeningExercise): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListeningExerciseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemListeningExerciseBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(exercise: ListeningExercise) {
            with(binding) {
                tvTitle.text = exercise.title
                tvDescription.text = exercise.description
                tvLevel.text = exercise.level
                tvDuration.text = "${exercise.duration}s"
                tvCategory.text = exercise.category

                root.setOnClickListener {
                    onExerciseClick(exercise)
                }
            }
        }
    }
}