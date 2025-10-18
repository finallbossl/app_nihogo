package com.example.nihongomaster.ui.reading

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nihongomaster.R
import com.example.nihongomaster.databinding.FragmentReadingListBinding
import com.example.nihongomaster.model.viewmodel.ReadingListViewModel

class ReadingListFragment : Fragment() {
    private var _b: FragmentReadingListBinding? = null
    private val b get() = _b!!
    private val vm: ReadingListViewModel by viewModels()
    private val adapter = ReadingCategoryAdapter { cat ->
        val args = Bundle().apply { putString("categoryId", cat.id) }
        findNavController().navigate(R.id.action_readingList_to_session, args)
    }

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentReadingListBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(v: View, s: Bundle?) {
        b.topBar.title = getString(R.string.reading)
        b.topBar.inflateMenu(R.menu.menu_home_top)
        b.recycler.layoutManager = LinearLayoutManager(requireContext())
        b.recycler.adapter = adapter
        vm.categories.observe(viewLifecycleOwner) { adapter.submitList(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView(); _b = null
    }
}