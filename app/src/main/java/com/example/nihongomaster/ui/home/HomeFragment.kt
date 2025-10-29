package com.example.nihongomaster.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nihongomaster.R
import com.example.nihongomaster.databinding.FragmentHomeBinding
import androidx.navigation.fragment.findNavController
import android.util.Log

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val vm: com.example.nihongomaster.model.viewmodel.HomeViewModel by viewModels()
    private val adapter = HomeAdapter { cardId ->
        when (cardId) {
            "vocab" -> findNavController().navigate(R.id.action_home_to_vocabularyList)
            "reading" -> findNavController().navigate(R.id.action_home_to_readingList)
            "listening" -> findNavController().navigate(R.id.action_home_to_listeningList)
            "tests" -> findNavController().navigate(R.id.action_home_to_testList)
            "progress" -> findNavController().navigate(R.id.progressDashboardFragment)


        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        Log.d("HomeFragment", "onViewCreated called")
        
        setupTopBar()
        setupRecyclerView()
        observeViewModel()
    }
    
    private fun setupTopBar() {
        binding.topBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_profile -> {
                    navigateSafely(R.id.profileFragment)
                    true
                }
                R.id.action_notifications -> {
                    navigateSafely(R.id.notificationsFragment)
                    true
                }
                else -> false
            }
        }
    }
    
    private fun setupRecyclerView() {
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HomeFragment.adapter
            setHasFixedSize(true)
        }
    }
    
    private fun observeViewModel() {
        vm.items.observe(viewLifecycleOwner) { items ->
            Log.d("HomeFragment", "Items received: ${items?.size ?: 0}")
            if (items != null && items.isNotEmpty()) {
                adapter.submitList(items)
                Log.d("HomeFragment", "Submitted ${items.size} items to adapter")
            } else {
                Log.w("HomeFragment", "Items is null or empty")
            }
        }
        
        vm.isLoading.observe(viewLifecycleOwner) { isLoading ->
            Log.d("HomeFragment", "Loading: $isLoading")
        }
        
        vm.error.observe(viewLifecycleOwner) { error ->
            if (error.isNotEmpty()) {
                Log.w("HomeFragment", "Error: $error")
            }
        }
    }
    
    private fun navigateSafely(destinationId: Int) {
        try {
            if (isAdded && !isDetached) {
                findNavController().navigate(destinationId)
            }
        } catch (e: Exception) {
            Log.e("HomeFragment", "Navigation error to $destinationId: ${e.message}")
        }
    }
}