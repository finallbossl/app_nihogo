package com.example.nihongomaster.ui.reading

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nihongomaster.R
import com.example.nihongomaster.databinding.ItemReadingCategoryBinding
import com.example.nihongomaster.model.ReadingCategory

class ReadingCategoryAdapter(
    private val onClick: (ReadingCategory) -> Unit
) : ListAdapter<ReadingCategory, ReadingCategoryAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<ReadingCategory>() {
        override fun areItemsTheSame(o: ReadingCategory, n: ReadingCategory) = o.id == n.id
        override fun areContentsTheSame(o: ReadingCategory, n: ReadingCategory) = o == n
    }

    inner class VH(val b: ItemReadingCategoryBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(m: ReadingCategory) {
            b.ivIcon.setImageResource(R.drawable.ic_article)
            b.tvTitle.text = m.title
            b.tvSubtitle.text = m.subtitle
            b.progress.setProgress((m.progress * 100).toInt(), false)
            b.root.setOnClickListener { onClick(m) }
        }
    }

    override fun onCreateViewHolder(p: ViewGroup, v: Int) =
        VH(ItemReadingCategoryBinding.inflate(LayoutInflater.from(p.context), p, false))
    override fun onBindViewHolder(h: VH, i: Int) = h.bind(getItem(i))
}
