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
import com.example.nihongomaster.model.viewmodel.ListeningListViewModel

class ListeningListFragment : Fragment() {

    private var _binding: FragmentListeningListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ListeningListViewModel by viewModels()

    // Adapter hiển thị từng bài nghe
    private val adapter = ListeningLessonAdapter { lesson ->
        val args = Bundle().apply { putString("lessonId", lesson.id) }
        // Điều hướng trực tiếp tới destination id (không phụ thuộc action)
        findNavController().navigate(R.id.listeningSessionFragment, args)
        // Hoặc nếu bạn có action trong nav graph:
        // findNavController().navigate(R.id.action_listeningList_to_session, args)
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
        // Toolbar
        binding.topBar.title = getString(R.string.listening)
        binding.topBar.inflateMenu(R.menu.menu_home_top)
        // Nếu muốn nút back:
        // binding.topBar.setNavigationIcon(com.google.android.material.R.drawable.material_ic_keyboard_arrow_left_black_24dp)
        // binding.topBar.setNavigationOnClickListener { findNavController().navigateUp() }

        // RecyclerView
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter

        // Quan sát dữ liệu
        viewModel.lessons.observe(viewLifecycleOwner) { lessons ->
            android.util.Log.d("ListeningListFragment", "Lessons received: ${lessons.size}")
            adapter.submitList(lessons)
        }
        
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            android.util.Log.d("ListeningListFragment", "Loading: $isLoading")
        }
        
        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error.isNotEmpty()) {
                android.widget.Toast.makeText(requireContext(), error, android.widget.Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
