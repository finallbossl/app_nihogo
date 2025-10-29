package com.example.nihongomaster.ui.vocab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nihongomaster.R
import com.example.nihongomaster.databinding.FragmentVocabSessionBinding

class VocabSessionFragment : Fragment() {

    private var _binding: FragmentVocabSessionBinding? = null
    private val binding get() = _binding!!
    private val vm: com.example.nihongomaster.model.viewmodel.VocabSessionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVocabSessionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val categoryId = arguments?.getString("categoryId") ?: "basic"

        // Header
        binding.topBar.setTitle(R.string.vocabulary)
        binding.topBar.setNavigationIcon(
            com.google.android.material.R.drawable.material_ic_keyboard_arrow_left_black_24dp
        )
        binding.topBar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.topBar.inflateMenu(R.menu.menu_home_top)

        // Start session (mock data for now)
        vm.start(categoryId)

        // Observers
        vm.current.observe(viewLifecycleOwner) { w ->
            if (w != null) {
                binding.tvJp.text = w.kanji
                binding.tvRomaji.text = w.hiragana
                binding.tvMeaning.text = w.meaning
                
                // Update favorite button state
                val isFav = com.example.nihongomaster.model.viewmodel.FavoriteManager.isFavorite(w.id)
                binding.btnFavorite.text = if (isFav) "❤️ Remove from Favorites" else "💖 Add to Favorites"
            }
        }

        vm.revealed.observe(viewLifecycleOwner) { shown ->
            binding.btnReveal.visibility = if (shown == true) View.GONE else View.VISIBLE
            binding.tvMeaning.visibility = if (shown == true) View.VISIBLE else View.INVISIBLE
        }

        vm.learned.observe(viewLifecycleOwner) { updateProgress() }
        vm.due.observe(viewLifecycleOwner) { updateProgress() }
        vm.total.observe(viewLifecycleOwner) { updateProgress() }
        
        vm.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // Hiển thị loading indicator nếu cần
            android.util.Log.d("VocabSessionFragment", "Loading: $isLoading")
        }
        
        vm.error.observe(viewLifecycleOwner) { error ->
            if (error.isNotEmpty()) {
                android.widget.Toast.makeText(requireContext(), error, android.widget.Toast.LENGTH_LONG).show()
            }
        }

        // Actions
        binding.btnReveal.setOnClickListener { vm.reveal() }
        binding.btnKnown.setOnClickListener { 
            vm.markKnown()
            // Auto add to favorites when marked as known
            val wordId = vm.currentWordId()
            if (wordId != null) {
                com.example.nihongomaster.model.viewmodel.FavoriteManager.addToFavorites(wordId)
            }
        }
        binding.btnUnknown.setOnClickListener { vm.markUnknown() }
        binding.btnSkip.setOnClickListener { vm.skip() }
        binding.btnNext.setOnClickListener { vm.skip() }
        
        // Favorite button
        binding.btnFavorite.setOnClickListener {
            val wordId = vm.currentWordId()
            if (wordId != null) {
                val isFav = com.example.nihongomaster.model.viewmodel.FavoriteManager.isFavorite(wordId)
                if (isFav) {
                    com.example.nihongomaster.model.viewmodel.FavoriteManager.removeFromFavorites(wordId)
                    binding.btnFavorite.text = "💖 Add to Favorites"
                } else {
                    com.example.nihongomaster.model.viewmodel.FavoriteManager.addToFavorites(wordId)
                    binding.btnFavorite.text = "❤️ Remove from Favorites"
                }
            }
        }

        // Details functionality removed - integrated into main flow
    }

    private fun updateProgress() {
        val learned = vm.learned.value ?: 0
        val total = vm.total.value ?: 0
        val due = vm.due.value ?: 0

        binding.tvLearned.text = learned.toString()
        binding.tvDue.text = due.toString()
        binding.tvTotal.text = total.toString()

        val ratio = if (total <= 0) 0f else learned.toFloat() / total
        binding.progress.setProgress((ratio * 100).toInt(), false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
