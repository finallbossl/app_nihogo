package com.example.nihongomaster.ui.vocab

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nihongomaster.R
import com.example.nihongomaster.databinding.FragmentVocabularyListBinding

class VocabularyListFragment : Fragment() {
    private var _binding: FragmentVocabularyListBinding? = null
    private val binding get() = _binding!!
    private val vm: com.example.nihongomaster.model.viewmodel.VocabularyListViewModel by viewModels()
    private val adapter = VocabCategoryAdapter { category ->
        val args = Bundle().apply { putString("categoryId", category.id) }
        findNavController().navigate(R.id.action_vocabularyList_to_session, args)
    }
    private val favoritesAdapter = VocabWordAdapter { word ->
        // Navigate to word detail or session
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentVocabularyListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, s: Bundle?) {
        binding.topBar.setTitle(R.string.vocabulary)
        binding.topBar.setNavigationIcon(com.google.android.material.R.drawable.material_ic_keyboard_arrow_left_black_24dp)
        binding.topBar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.topBar.inflateMenu(R.menu.menu_home_top)

        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter
        
        binding.recyclerFavorites.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerFavorites.adapter = favoritesAdapter

        vm.categories.observe(viewLifecycleOwner) { adapter.submitList(it) }
        vm.favoriteWords.observe(viewLifecycleOwner) { favorites ->
            binding.cardFavorites.visibility = if (favorites.isEmpty()) View.GONE else View.VISIBLE
            favoritesAdapter.submitList(favorites)
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}