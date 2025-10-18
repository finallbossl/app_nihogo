package com.example.nihongomaster.ui.reading

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nihongomaster.R
import com.example.nihongomaster.databinding.FragmentReadingSessionBinding
import com.example.nihongomaster.model.viewmodel.ReadingSessionViewModel

class ReadingSessionFragment : Fragment() {
    private var _b: FragmentReadingSessionBinding? = null
    private val b get() = _b!!
    private val vm: ReadingSessionViewModel by viewModels()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentReadingSessionBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(v: View, s: Bundle?) {
        val categoryId = arguments?.getString("categoryId") ?: "intermediate"

        b.topBar.title = getString(R.string.reading)
        b.topBar.setNavigationIcon(
            com.google.android.material.R.drawable.material_ic_keyboard_arrow_left_black_24dp
        )
        b.topBar.setNavigationOnClickListener { findNavController().navigateUp() }
        b.topBar.inflateMenu(R.menu.menu_home_top)

        vm.start(categoryId)

        vm.current.observe(viewLifecycleOwner) { art ->
            b.tvTitle.text = art?.title ?: ""
            b.tvContent.text = art?.jpText ?: ""
        }
        vm.pos.observe(viewLifecycleOwner) { p -> b.tvPos.text = p.toString() }
        vm.total.observe(viewLifecycleOwner) { t -> b.tvTotal.text = t.toString() }

        b.btnPrev.setOnClickListener { vm.previous() }
        b.btnNext.setOnClickListener { vm.next() }
        b.btnDetails.setOnClickListener {
            vm.currentArticleId()?.let { id ->
                val args = Bundle().apply { putString("articleId", id) }
                findNavController().navigate(R.id.action_readingSession_to_detail, args)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView(); _b = null
    }
}