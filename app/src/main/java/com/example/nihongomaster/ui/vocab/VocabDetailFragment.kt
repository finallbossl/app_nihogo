package com.example.nihongomaster.ui.vocab

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nihongomaster.databinding.FragmentVocabDetailBinding

class VocabDetailFragment : Fragment() {
    private var _binding: FragmentVocabDetailBinding? = null
    private val binding get() = _binding!!
    private val vm: com.example.nihongomaster.model.viewmodel.VocabDetailViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentVocabDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, s: Bundle?) {
        // Header + nút back
        binding.topBar.setTitle("Vocabulary Details")
        binding.topBar.setNavigationIcon(com.google.android.material.R.drawable.material_ic_keyboard_arrow_left_black_24dp)
        binding.topBar.setNavigationOnClickListener { findNavController().popBackStack() }
        binding.topBar.inflateMenu(com.example.nihongomaster.R.menu.menu_home_top)

        // Load mock
        val wordId = requireArguments().getString("wordId") ?: "basic_1"
        vm.load(wordId)

        vm.detail.observe(viewLifecycleOwner) { d ->
            binding.tvHeadword.text = d.headwordJp
            binding.tvRomaji.text = d.romaji
            binding.tvIpa.text = d.ipa
            binding.tvDefShort.text = d.definitionShort
            binding.tvDefLong.text = d.definitionLong

            // Examples
            binding.examplesContainer.removeAllViews()
            d.examples.forEach {
                val item =
                    com.google.android.material.textview.MaterialTextView(requireContext()).apply {
                        text = "${it.jp}\n${it.en}"
                        textSize = 14f
                        setPadding(0, 12, 0, 12)
                    }
                binding.examplesContainer.addView(item)
            }

            // Word details
            binding.tvLevel.text = "N5"
            binding.tvCategory.text = "Basic Vocabulary"
        }

        // Favorite state
        vm.isFavorite.observe(viewLifecycleOwner) { isFav ->
            binding.btnFavorite.text =
                if (isFav) "💖 Remove from Favorites" else "💖 Add to Favorites"
        }

        // Actions
        binding.btnFavorite.setOnClickListener { vm.toggleFavorite() }
        binding.btnReview.setOnClickListener { /* TODO: mark for review */ }
        binding.btnPlayAudio.setOnClickListener { /* TODO: play audio */ }
    }

    override fun onDestroyView() {
        super.onDestroyView(); _binding = null
    }
}