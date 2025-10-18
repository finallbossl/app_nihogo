package com.example.nihongomaster.ui.progress

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nihongomaster.R
import com.example.nihongomaster.databinding.FragmentProgressDashboardBinding

class ProgressDashboardFragment : Fragment() {
    private var _b: FragmentProgressDashboardBinding? = null
    private val b get() = _b!!
    private val vm: com.example.nihongomaster.model.viewmodel.ProgressViewModel by viewModels()

    private val moduleAdapter = StudyModuleAdapter()
    private val favoriteWordAdapter = FavoriteWordAdapter()
    private val ongoingLessonAdapter = OngoingLessonAdapter()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentProgressDashboardBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        b.topBar.title = getString(R.string.progress_dashboard)
        b.topBar.setNavigationIcon(com.google.android.material.R.drawable.material_ic_keyboard_arrow_left_black_24dp)
        b.topBar.setNavigationOnClickListener { requireActivity().onBackPressed() }

        // Stats
        vm.metrics.observe(viewLifecycleOwner) {
            b.tvWords.text = it.wordsLearned.toString()
            b.tvLessons.text = it.lessonsCompleted.toString()
            b.tvStudyTime.text = "${it.studyTimeMinutes / 60}h"
        }

        // Modules
        b.rvModules.layoutManager = LinearLayoutManager(requireContext())
        b.rvModules.adapter = moduleAdapter
        vm.studyModules.observe(viewLifecycleOwner) { moduleAdapter.submitList(it) }

        // Ongoing lessons
        b.rvOngoingLessons.layoutManager = LinearLayoutManager(requireContext())
        b.rvOngoingLessons.adapter = ongoingLessonAdapter
        vm.ongoingLessons.observe(viewLifecycleOwner) { ongoingLessonAdapter.submitList(it) }

        // Favorite words
        b.rvFavoriteWords.layoutManager = LinearLayoutManager(requireContext())
        b.rvFavoriteWords.adapter = favoriteWordAdapter
        vm.favoriteWords.observe(viewLifecycleOwner) { favoriteWordAdapter.submitList(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView(); _b = null
    }
}