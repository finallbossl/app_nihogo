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
    private val vm: VocabularyListViewModel by viewModels()
    private val adapter = VocabCategoryAdapter { category ->
        val args = Bundle().apply { putString("categoryId", category.id) }
        findNavController().navigate(R.id.action_vocabularyList_to_session, args)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentVocabularyListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, s: Bundle?) {
        binding.topBar.setTitle(R.string.vocabulary)
        binding.topBar.inflateMenu(R.menu.menu_home_top)

        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter

        vm.categories.observe(viewLifecycleOwner) { adapter.submitList(it) }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}