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
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val vm: HomeViewModel by viewModels()
    private val adapter = HomeAdapter { cardId ->
        when(cardId){
            "vocab" -> findNavController().navigate(R.id.action_home_to_vocabularyList)
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
        binding.topBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_notifications -> {
                    // TODO: Mở màn thông báo
                    true
                }
                R.id.action_profile -> {
                    // TODO: Mở màn hồ sơ
                    true
                }
                else -> false
            }
        }

        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter
        vm.items.observe(viewLifecycleOwner) { adapter.submitList(it) }
    }
}