package com.example.nihongomaster.ui.vocab

import com.example.nihongomaster.model.VocabWord

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nihongomaster.databinding.ItemVocabWordBinding

class VocabWordAdapter(
    private val onWordClick: (VocabWord) -> Unit
) : ListAdapter<VocabWord, VocabWordAdapter.ViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<VocabWord>() {
        override fun areItemsTheSame(oldItem: VocabWord, newItem: VocabWord): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: VocabWord, newItem: VocabWord): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemVocabWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemVocabWordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(word: VocabWord) {
            binding.tvKanji.text = word.kanji
            binding.tvHiragana.text = word.hiragana
            binding.tvMeaning.text = word.meaning
            binding.root.setOnClickListener { onWordClick(word) }
        }
    }
}