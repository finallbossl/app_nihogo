package com.example.nihongomaster.ui.listening

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nihongomaster.R
import com.example.nihongomaster.databinding.ItemListeningLessonBinding
import com.example.nihongomaster.model.ListeningLesson

class ListeningLessonAdapter(
    private val onClick: (ListeningLesson) -> Unit
) : ListAdapter<ListeningLesson, ListeningLessonAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<ListeningLesson>() {
        override fun areItemsTheSame(o: ListeningLesson, n: ListeningLesson) = o.id == n.id
        override fun areContentsTheSame(o: ListeningLesson, n: ListeningLesson) = o == n
    }

    inner class VH(val b: ItemListeningLessonBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(m: ListeningLesson) {
            b.tvTitle.text = m.title
            b.tvLevel.text = m.level
            b.tvDuration.text = "${m.audioDurationSec/60}:${(m.audioDurationSec%60).toString().padStart(2,'0')}"
            b.root.setOnClickListener { onClick(m) }
        }
    }

    override fun onCreateViewHolder(p: ViewGroup, v: Int) =
        VH(ItemListeningLessonBinding.inflate(LayoutInflater.from(p.context), p, false))
    override fun onBindViewHolder(h: VH, i: Int) = h.bind(getItem(i))
}
