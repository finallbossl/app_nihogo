package com.example.nihongomaster.ui.vocab

import com.example.nihongomaster.model.VocabCategory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nihongomaster.databinding.ItemVocabCategoryBinding
import com.google.android.material.progressindicator.LinearProgressIndicator

class VocabCategoryAdapter(
    private val onClick: (VocabCategory) -> Unit
) : ListAdapter<VocabCategory, VocabCategoryAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<VocabCategory>() {
        override fun areItemsTheSame(o: VocabCategory, n: VocabCategory) = o.id == n.id
        override fun areContentsTheSame(o: VocabCategory, n: VocabCategory) = o == n
    }

    inner class VH(val b: ItemVocabCategoryBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(m: VocabCategory) {
            b.ivIcon.setImageResource(m.iconRes)
            b.tvTitle.text = m.title
            b.tvSubtitle.text = m.subtitle
            b.progress.setProgress((m.progress * 100).toInt(), false)
            b.root.setOnClickListener { onClick(m) }
        }
    }

    override fun onCreateViewHolder(p: ViewGroup, v: Int): VH =
        VH(ItemVocabCategoryBinding.inflate(LayoutInflater.from(p.context), p, false))
    override fun onBindViewHolder(h: VH, i: Int) = h.bind(getItem(i))
}