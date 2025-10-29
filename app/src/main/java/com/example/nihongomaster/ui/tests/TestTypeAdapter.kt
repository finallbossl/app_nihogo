package com.example.nihongomaster.ui.tests

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nihongomaster.R
import com.example.nihongomaster.databinding.ItemTestTypeBinding
import com.example.nihongomaster.model.TestType

class TestTypeAdapter(
    private val onClick: (TestType) -> Unit
) : ListAdapter<TestType, TestTypeAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<TestType>() {
        override fun areItemsTheSame(o: TestType, n: TestType) = o.id == n.id
        override fun areContentsTheSame(o: TestType, n: TestType) = o == n
    }

    inner class VH(val b: ItemTestTypeBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(m: TestType) {
            b.tvTitle.text = m.title
            b.tvSubtitle.text = m.subtitle
            b.ivIcon.setImageResource(m.iconRes)
            b.root.setOnClickListener { onClick(m) }
        }
    }

    override fun onCreateViewHolder(p: ViewGroup, v: Int) =
        VH(ItemTestTypeBinding.inflate(LayoutInflater.from(p.context), p, false))

    override fun onBindViewHolder(h: VH, i: Int) = h.bind(getItem(i))
}
