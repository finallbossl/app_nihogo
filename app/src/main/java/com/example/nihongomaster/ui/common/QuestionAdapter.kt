package com.example.nihongomaster.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nihongomaster.databinding.ItemQuestionBinding
import com.example.nihongomaster.model.Question

class QuestionAdapter(
    private val onAnswerSelected: (String, Int) -> Unit
) : ListAdapter<Question, QuestionAdapter.ViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<Question>() {
        override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemQuestionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position + 1)
    }

    inner class ViewHolder(
        private val binding: ItemQuestionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(question: Question, questionNumber: Int) {
            with(binding) {
                tvQuestionNumber.text = "Câu $questionNumber"
                tvQuestion.text = question.question
                
                radioGroup.removeAllViews()
                
                question.options.forEachIndexed { index, option ->
                    val radioButton = android.widget.RadioButton(binding.root.context).apply {
                        text = "${('A' + index)}) $option"
                        id = index
                    }
                    radioGroup.addView(radioButton)
                }
                
                radioGroup.setOnCheckedChangeListener { _, checkedId ->
                    if (checkedId != -1) {
                        onAnswerSelected(question.id, checkedId)
                    }
                }
            }
        }
    }
}