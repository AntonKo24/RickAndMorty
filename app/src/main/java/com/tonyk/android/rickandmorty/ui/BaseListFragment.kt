package com.tonyk.android.rickandmorty.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tonyk.android.rickandmorty.databinding.FragmentMainListBinding
import com.tonyk.android.rickandmorty.util.NetworkChecker
import kotlinx.coroutines.launch

abstract class BaseListFragment<T : Any, VH : RecyclerView.ViewHolder> : Fragment() {
    private var _binding: FragmentMainListBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val status = NetworkChecker.isNetworkAvailable(requireContext())
        checkStatus(status)

        val adapter = createAdapter()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectData(adapter)
            }
        }
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerView.adapter = adapter

        binding.filters.setOnClickListener {
            navigateToFilterFragment()
        }
        lifecycleScope.launch {
            observeLoadState(adapter)
        }

        binding.SwipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract suspend fun observeLoadState(adapter: PagingDataAdapter<T, VH>)
    abstract fun createAdapter(): PagingDataAdapter<T, VH>
    abstract fun collectData(adapter: PagingDataAdapter<T, VH>)
    abstract fun navigateToFilterFragment()
    abstract fun refreshData()
    abstract fun checkStatus(status : Boolean)
}