package com.example.nihongomaster.ui.vocab


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.nihongomaster.R
import com.example.nihongomaster.databinding.FragmentVocabSessionBinding

class VocabSessionFragment : Fragment() {
    private var _binding: FragmentVocabSessionBinding? = null
    private val binding get() = _binding!!
    private val vm: VocabSessionViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentVocabSessionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, s: Bundle?) {
        val categoryId = requireArguments().getString("categoryId") ?: "basic"
        binding.topBar.setTitle(R.string.vocabulary)
        binding.topBar.inflateMenu(R.menu.menu_home_top)

        vm.start(categoryId)

        vm.current.observe(viewLifecycleOwner) { w ->
            binding.tvJp.text = w.jp
            binding.tvRomaji.text = w.romaji
            binding.tvMeaning.text = w.meaning
        }
        vm.revealed.observe(viewLifecycleOwner) { shown ->
            binding.btnReveal.visibility = if (shown) View.GONE else View.VISIBLE
            binding.tvMeaning.visibility = if (shown) View.VISIBLE else View.INVISIBLE
        }
        vm.learned.observe(viewLifecycleOwner) { updateProgress() }
        vm.due.observe(viewLifecycleOwner) { updateProgress() }
        vm.total.observe(viewLifecycleOwner) { updateProgress() }

        binding.btnReveal.setOnClickListener { vm.reveal() }
        binding.btnKnown.setOnClickListener { vm.markKnown() }
        binding.btnUnknown.setOnClickListener { vm.markUnknown() }
        binding.btnSkip.setOnClickListener { vm.skip() }
        binding.btnNext.setOnClickListener { vm.skip() }
    }

    private fun updateProgress() {
        val learned = vm.learned.value ?: 0
        val due = vm.due.value ?: 0
        val total = vm.total.value ?: 0
        binding.tvLearned.text = learned.toString()
        binding.tvDue.text = due.toString()
        binding.tvTotal.text = total.toString()
        val ratio = if (total == 0) 0f else learned.toFloat() / total
        binding.progress.setProgress((ratio * 100).toInt(), false)
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}