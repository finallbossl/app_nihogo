package com.example.nihongomaster.ui.listening

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android:view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nihongomaster.databinding.FragmentListeningDetailBinding
import com.example.nihongomaster.model.viewmodel.ListeningViewModel
import com.example.nihongomaster.ui.common.QuestionAdapter

class ListeningDetailFragment : Fragment() {
    private var _binding: FragmentListeningDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ListeningViewModel by viewModels()
    private val questionAdapter = QuestionAdapter { questionId, selectedOption ->
        // Handle answer selection
        userAnswers[questionId] = selectedOption
        checkSubmitButtonState()
    }
    
    private val userAnswers = mutableMapOf<String, Int>()
    private var listeningCount = 0
    private var isTranscriptVisible = false
    private var startTime = System.currentTimeMillis()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListeningDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val exerciseId = arguments?.getString("exerciseId") ?: return
        
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
        setupToolbar()
        
        // Load exercise detail
        viewModel.loadExerciseDetail(exerciseId)
    }

    private fun setupRecyclerView() {
        binding.recyclerQuestions.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerQuestions.adapter = questionAdapter
    }

    private fun setupObservers() {
        viewModel.currentExercise.observe(viewLifecycleOwner) { exercise ->
            exercise?.let {
                binding.tvTitle.text = it.title
                binding.tvDescription.text = it.description
                binding.tvInstructions.text = it.instructions ?: ""
                binding.tvTranscript.text = it.transcript ?: ""
                questionAdapter.submitList(it.questions)
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            // Handle loading state
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                viewModel.clearError()
            }
        }

        viewModel.submitResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(context, "Đã nộp bài thành công!", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnPlay.setOnClickListener {
            // TODO: Implement audio playback
            listeningCount++
            binding.tvListenCount.text = "Đã nghe: $listeningCount/3 lần"
            
            if (listeningCount >= 3) {
                binding.btnPlay.isEnabled = false
            }
            
            Toast.makeText(context, "Phát audio (demo)", Toast.LENGTH_SHORT).show()
        }

        binding.btnShowTranscript.setOnClickListener {
            isTranscriptVisible = !isTranscriptVisible
            binding.cardTranscript.visibility = if (isTranscriptVisible) View.VISIBLE else View.GONE
            binding.btnShowTranscript.text = if (isTranscriptVisible) "Ẩn Transcript" else "Hiển thị Transcript"
        }

        binding.btnSubmit.setOnClickListener {
            val exercise = viewModel.currentExercise.value ?: return@setOnClickListener
            val timeSpent = ((System.currentTimeMillis() - startTime) / 1000).toInt()
            
            viewModel.submitAnswers(exercise.id, userAnswers, timeSpent, listeningCount)
        }
    }

    private fun setupToolbar() {
        binding.topBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun checkSubmitButtonState() {
        val exercise = viewModel.currentExercise.value
        val hasAllAnswers = exercise?.questions?.all { userAnswers.containsKey(it.id) } ?: false
        binding.btnSubmit.isEnabled = hasAllAnswers && listeningCount > 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}