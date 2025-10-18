package com.example.nihongomaster.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nihongomaster.R
import com.example.nihongomaster.databinding.FragmentProfileBinding
import com.example.nihongomaster.ui.progress.AchievementAdapter
import com.example.nihongomaster.ui.progress.RecentActivityAdapter

class ProfileFragment : Fragment() {

    private var _b: FragmentProfileBinding? = null
    private val b get() = _b!!
    private val vm: com.example.nihongomaster.model.viewmodel.ProfileViewModel by viewModels()

    private val activityAdapter = RecentActivityAdapter()
    private val achievementAdapter = AchievementAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _b = FragmentProfileBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        b.topBar.title = getString(R.string.profile)
        b.topBar.setNavigationIcon(com.google.android.material.R.drawable.material_ic_keyboard_arrow_left_black_24dp)
        b.topBar.setNavigationOnClickListener { requireActivity().onBackPressed() }
        b.topBar.inflateMenu(R.menu.menu_home_top)

        // Bind header
        vm.name.observe(viewLifecycleOwner) { b.tvName.text = it }
        vm.level.observe(viewLifecycleOwner) { b.tvLevel.text = it }

        // Stats
        vm.streak.observe(viewLifecycleOwner) { b.tvStreak.text = it.toString() }
        vm.words.observe(viewLifecycleOwner) { b.tvWords.text = it.toString() }
        vm.accuracy.observe(viewLifecycleOwner) { b.tvAccuracy.text = "$it%" }

        // Summary
        vm.summary.observe(viewLifecycleOwner) { b.tvSummary.text = it }
        vm.summaryProgress.observe(viewLifecycleOwner) { b.summaryProgress.setProgress(it, true) }

        // Recent activity
        b.rvActivity.layoutManager = LinearLayoutManager(requireContext())
        b.rvActivity.adapter = activityAdapter
        vm.activities.observe(viewLifecycleOwner) { activityAdapter.submitList(it) }

        // Achievements
        b.rvAchievements.layoutManager = GridLayoutManager(requireContext(), 3)
        b.rvAchievements.adapter = achievementAdapter
        vm.achievements.observe(viewLifecycleOwner) { achievementAdapter.submitList(it) }

        // Edit name action
        b.btnEdit.setOnClickListener {
            showEditNameDialog()
        }
    }

    private fun showEditNameDialog() {
        val editText = android.widget.EditText(requireContext()).apply {
            setText(vm.name.value)
            hint = "Enter your name"
            setPadding(48, 32, 48, 32)
        }

        com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
            .setTitle("Edit Name").setView(editText).setPositiveButton("Save") { _, _ ->
                val newName = editText.text.toString().trim()
                if (newName.isNotEmpty()) {
                    vm.updateName(newName)
                }
            }.setNegativeButton("Cancel", null).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}