package com.example.nihongomaster.ui.progress

import com.example.nihongomaster.model.*

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nihongomaster.databinding.ItemAchievementBinding

class AchievementAdapter :
    ListAdapter<Achievement, AchievementAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<Achievement>() {
        override fun areItemsTheSame(o: Achievement, n: Achievement) = o.id == n.id
        override fun areContentsTheSame(o: Achievement, n: Achievement) = o == n
    }

    inner class VH(val b: ItemAchievementBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(m: Achievement) {
            b.ivIcon.setImageResource(m.iconRes)
            b.tvTitle.text = m.title
            b.tvSubtitle.text = m.subtitle
            b.badgeDone.isVisible = m.achieved
        }
    }

    override fun onCreateViewHolder(p: ViewGroup, v: Int) =
        VH(ItemAchievementBinding.inflate(LayoutInflater.from(p.context), p, false))

    override fun onBindViewHolder(h: VH, i: Int) = h.bind(getItem(i))
}