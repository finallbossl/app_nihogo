package com.example.nihongomaster.ui.listening

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nihongomaster.R
import com.example.nihongomaster.databinding.FragmentListeningListBinding
import com.example.nihongomaster.model.viewmodel.ListeningViewModel

class ListeningListFragment : Fragment() {
    private var _binding: FragmentListeningListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ListeningViewModel by viewModels()
    private val adapter = ListeningExerciseAdapter { exercise ->
        val args = Bundle().apply { 
            putString("exerciseId", exercise.id)
        }
        findNavController().navigate(R.id.action_listeningList_to_detail, args)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListeningListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupObservers()
        setupToolbar()
        
        // Load data
        viewModel.loadExercises()
    }

    private fun setupRecyclerView() {
        binding.recyclerListening.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerListening.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.exercises.observe(viewLifecycleOwner) { exercises ->
            adapter.submitList(exercises)
        }
    }

    private fun setupToolbar() {
        binding.topBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}