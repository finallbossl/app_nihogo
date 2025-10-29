package com.example.nihongomaster.ui.tests

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nihongomaster.R
import com.example.nihongomaster.databinding.FragmentTestListBinding
import com.example.nihongomaster.model.viewmodel.TestListViewModel

class TestListFragment : Fragment() {
    private var _b: FragmentTestListBinding? = null
    private val b get() = _b!!
    private val vm: TestListViewModel by viewModels()

    private val adapter = TestTypeAdapter { type ->
        val args = Bundle().apply { putString("testTypeId", type.id) }
        findNavController().navigate(R.id.testSessionFragment, args)
    }

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentTestListBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(v: View, s: Bundle?) {
        b.topBar.title = getString(R.string.tests)
        b.topBar.inflateMenu(R.menu.menu_home_top)

        b.recycler.layoutManager = LinearLayoutManager(requireContext())
        b.recycler.adapter = adapter

        vm.items.observe(viewLifecycleOwner) { list -> adapter.submitList(list) }
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
