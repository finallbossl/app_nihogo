package com.example.nihongomaster.ui.progress

import com.example.nihongomaster.model.*

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nihongomaster.databinding.ItemFavoriteWordBinding

class FavoriteWordAdapter : ListAdapter<FavoriteWord, FavoriteWordAdapter.ViewHolder>(Diff) {

    object Diff : DiffUtil.ItemCallback<FavoriteWord>() {
        override fun areItemsTheSame(oldItem: FavoriteWord, newItem: FavoriteWord): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: FavoriteWord, newItem: FavoriteWord): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemFavoriteWordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemFavoriteWordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(word: FavoriteWord) {
            binding.apply {
                tvKanji.text = word.kanji
                tvHiragana.text = word.hiragana
                tvMeaning.text = word.meaning
                chipLevel.text = word.level
            }
        }
    }
}