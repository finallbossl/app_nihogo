package com.example.nihongomaster.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nihongomaster.R
import com.example.nihongomaster.databinding.FragmentNotificationsBinding
import com.example.nihongomaster.model.viewmodel.NotificationsViewModel

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NotificationsViewModel by viewModels()

    private val adapter = NotificationAdapter { notification ->
        // Handle notification click
        viewModel.markAsRead(notification.id)
        // TODO: Navigate to relevant screen based on notification type
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupMenu()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.rvNotifications.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@NotificationsFragment.adapter
        }
    }

    private fun setupMenu() {
        binding.topBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_mark_all_read -> {
                    // TODO: Mark all as read
                    true
                }

                R.id.action_clear_all -> {
                    viewModel.clearAll()
                    true
                }

                else -> false
            }
        }
    }

    private fun observeViewModel() {
        viewModel.notifications.observe(viewLifecycleOwner) { notifications ->
            adapter.submitList(notifications)
            binding.layoutEmpty.visibility =
                if (notifications.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}