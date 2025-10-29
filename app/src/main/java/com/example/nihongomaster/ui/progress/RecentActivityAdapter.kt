package com.example.nihongomaster.ui.progress

import com.example.nihongomaster.model.*

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nihongomaster.databinding.ItemRecentActivityBinding

class RecentActivityAdapter :
    ListAdapter<ActivityItem, RecentActivityAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<ActivityItem>() {
        override fun areItemsTheSame(o: ActivityItem, n: ActivityItem) = o.id == n.id
        override fun areContentsTheSame(o: ActivityItem, n: ActivityItem) = o == n
    }

    inner class VH(val b: ItemRecentActivityBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(m: ActivityItem) {
            b.ivIcon.setImageResource(m.iconRes)
            b.tvTitle.text = m.title
            b.tvSubtitle.text = m.subtitle
            b.tvTime.text = m.timeAgo
            if (m.tag.isNullOrBlank()) {
                b.chipTag.text = ""
                b.chipTag.visibility = android.view.View.GONE
            } else {
                b.chipTag.text = m.tag
                b.chipTag.visibility = android.view.View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(p: ViewGroup, v: Int) =
        VH(ItemRecentActivityBinding.inflate(LayoutInflater.from(p.context), p, false))
    override fun onBindViewHolder(h: VH, i: Int) = h.bind(getItem(i))
}
