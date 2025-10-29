package com.example.nihongomaster.ui.reading

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nihongomaster.databinding.FragmentReadingDetailBinding
import com.example.nihongomaster.model.viewmodel.ReadingDetailViewModel

class ReadingDetailFragment : Fragment() {
    private var _b: FragmentReadingDetailBinding? = null
    private val b get() = _b!!
    private val vm: ReadingDetailViewModel by viewModels()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentReadingDetailBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(v: View, s: Bundle?) {
        b.topBar.title = "Reading Details"
        b.topBar.setNavigationIcon(
            com.google.android.material.R.drawable.material_ic_keyboard_arrow_left_black_24dp)
        b.topBar.setNavigationOnClickListener { findNavController().navigateUp() }

        val categoryId = arguments?.getString("categoryId") ?: "beginner"
        val articleId = arguments?.getString("articleId") ?: "1"
        vm.load(categoryId, articleId)

        vm.detail.observe(viewLifecycleOwner) { d ->
            b.tvTitle.text = d.title
            b.tvTextFull.text = d.jpText
            b.tvRomaji.text = d.romajiNotes
            b.vocabContainer.removeAllViews()
            d.vocabNotes.forEach { (jp, en) ->
                val tv = com.google.android.material.textview.MaterialTextView(requireContext()).apply {
                    text = "$jp — $en"
                    textSize = 14f
                    setPadding(0, 8, 0, 8)
                }
                b.vocabContainer.addView(tv)
            }
        }
        
        vm.isLoading.observe(viewLifecycleOwner) { isLoading ->
            android.util.Log.d("ReadingDetailFragment", "Loading: $isLoading")
        }
        
        vm.error.observe(viewLifecycleOwner) { error ->
            if (error.isNotEmpty()) {
                android.widget.Toast.makeText(requireContext(), error, android.widget.Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView(){ super.onDestroyView(); _b=null }
}
