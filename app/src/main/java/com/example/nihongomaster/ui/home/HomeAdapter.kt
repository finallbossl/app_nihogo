package com.example.nihongomaster.ui.home

import com.example.nihongomaster.model.HomeUiModel


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nihongomaster.databinding.ItemFeatureCardBinding
import com.example.nihongomaster.databinding.ItemSectionHeaderBinding
import com.example.nihongomaster.databinding.ItemWelcomeBinding


class HomeAdapter(
    private val onCardClick: (id: String) -> Unit
) : ListAdapter<HomeUiModel, RecyclerView.ViewHolder>(Diff) {

    object Diff : DiffUtil.ItemCallback<HomeUiModel>() {
        override fun areItemsTheSame(oldItem: HomeUiModel, newItem: HomeUiModel): Boolean =
            oldItem::class == newItem::class &&
                    (oldItem as? HomeUiModel.FeatureCard)?.id == (newItem as? HomeUiModel.FeatureCard)?.id

        override fun areContentsTheSame(oldItem: HomeUiModel, newItem: HomeUiModel): Boolean = oldItem == newItem
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is HomeUiModel.Welcome -> 0
        is HomeUiModel.SectionHeader -> 1
        is HomeUiModel.FeatureCard -> 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inf = LayoutInflater.from(parent.context)
        return when (viewType) {
            0 -> WelcomeVH(ItemWelcomeBinding.inflate(inf, parent, false))
            1 -> SectionVH(ItemSectionHeaderBinding.inflate(inf, parent, false))
            else -> CardVH(ItemFeatureCardBinding.inflate(inf, parent, false), onCardClick)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is HomeUiModel.Welcome -> (holder as WelcomeVH).bind(item)
            is HomeUiModel.SectionHeader -> (holder as SectionVH).bind(item)
            is HomeUiModel.FeatureCard -> (holder as CardVH).bind(item)
        }
    }

    class WelcomeVH(private val b: ItemWelcomeBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(m: HomeUiModel.Welcome) {
            b.tvTitle.text = m.title
            b.tvSubtitle.text = m.subtitle
            b.ivHero.setImageResource(m.imageRes)
        }
    }

    class SectionVH(private val b: ItemSectionHeaderBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(m: HomeUiModel.SectionHeader) { b.tvSection.text = m.title }
    }

    class CardVH(
        private val b: ItemFeatureCardBinding,
        private val onClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(b.root) {
        fun bind(m: HomeUiModel.FeatureCard) {
            b.ivIcon.setImageResource(m.iconRes)
            b.tvTitle.text = m.title
            b.tvSubtitle.text = m.subtitle
            b.root.setOnClickListener { onClick(m.id) }
        }
    }
}