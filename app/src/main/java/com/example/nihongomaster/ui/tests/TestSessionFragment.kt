package com.example.nihongomaster.ui.tests

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nihongomaster.R
import com.example.nihongomaster.databinding.FragmentTestSessionBinding
import com.example.nihongomaster.model.TestQuestion
import com.example.nihongomaster.model.viewmodel.TestSessionViewModel
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.LinearProgressIndicator

class TestSessionFragment : Fragment() {
    private var _b: FragmentTestSessionBinding? = null
    private val b get() = _b!!
    private val vm: TestSessionViewModel by viewModels()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentTestSessionBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(v: View, s: Bundle?) {
        b.topBar.title = getString(R.string.tests)
        b.topBar.setNavigationIcon(com.google.android.material.R.drawable.material_ic_keyboard_arrow_left_black_24dp)
        b.topBar.setNavigationOnClickListener { findNavController().navigateUp() }
        b.topBar.inflateMenu(R.menu.menu_home_top)

        val typeId = arguments?.getString("testTypeId") ?: "mcq"
        vm.start(typeId)

        vm.total.observe(viewLifecycleOwner) { total ->
            b.tvProgressLabel.text = getString(R.string.question_x_of_y, vm.position.value ?: 1, total)
            b.progress.max = total
        }
        vm.position.observe(viewLifecycleOwner) { pos ->
            b.tvProgressLabel.text = getString(R.string.question_x_of_y, pos, vm.total.value ?: 0)
            b.progress.setProgress(pos, true)
        }
        vm.current.observe(viewLifecycleOwner) { q ->
            if (q == null) return@observe
            bindQuestion(q)
            setNextEnabled(vm.hasChosen())
        }

        b.btnNext.setOnClickListener {
            if (vm.next()) {
                // sang câu tiếp theo
            } else {
                // hết câu → chấm điểm
                val (correct, total) = vm.grade()
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.your_score))
                    .setMessage(getString(R.string.score_format, correct, total, if (total==0) 0 else correct*100/total))
                    .setPositiveButton(android.R.string.ok, null)
                    .show()
            }
        }
    }

    private fun setNextEnabled(enable: Boolean) {
        b.btnNext.isEnabled = enable
        b.btnNext.alpha = if (enable) 1f else 0.5f
    }

    private fun bindQuestion(q: TestQuestion) {
        b.tvQuestionTitle.text = q.text

        val cards = listOf(b.optA, b.optB, b.optC, b.optD)
        val labels = listOf(b.tvA, b.tvB, b.tvC, b.tvD)

        // gán text và ẩn nếu không có
        labels.forEachIndexed { idx, tv ->
            val text = q.options.getOrNull(idx)
            tv.text = text ?: ""
            cards[idx].isVisible = text != null
            setSelected(cards[idx], false)
            cards[idx].setOnClickListener {
                cards.forEach { c -> setSelected(c, false) }
                setSelected(cards[idx], true)
                vm.choose(idx)
                setNextEnabled(true)
            }
        }
        // nếu chưa chọn -> khoá Next
        setNextEnabled(vm.hasChosen())
    }

    private fun setSelected(card: MaterialCardView, selected: Boolean) {
        val color = try { requireContext().getColor(R.color.brandPrimary) } catch (_: Exception) { 0xFF6200EE.toInt() }
        val stroke = if (selected) color else 0x1A000000.toInt()
        val width = if (selected) 2 else 1
        card.strokeColor = stroke
        card.strokeWidth = (width * resources.displayMetrics.density).toInt()
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
