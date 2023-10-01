package com.tonyk.android.rickandmorty.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tonyk.android.rickandmorty.databinding.FragmentMainListBinding
import com.tonyk.android.rickandmorty.util.NetworkChecker
import com.tonyk.android.rickandmorty.viewmodel.BaseListViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseListFragment<T : Any, VH : RecyclerView.ViewHolder> : Fragment() {
    private var _binding: FragmentMainListBinding? = null
    private val binding get() = _binding!!

    protected abstract val viewModel: BaseListViewModel<T, *>

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
        updateStatusAndText(status)

        val adapter = createAdapter()
        observeData(adapter)

        setupRecyclerView(adapter)

        binding.filters.setOnClickListener {
            navigateToFilterFragment()
        }

        observeLoadState(adapter)

        setupRefreshListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun createAdapter(): PagingDataAdapter<T, VH>
    abstract fun navigateToFilterFragment()

    private fun setupRecyclerView(adapter: PagingDataAdapter<T, VH>) {
        binding.apply {
            recyclerView.layoutManager = GridLayoutManager(context, 2)
            recyclerView.adapter = adapter
        }
    }

    private fun setupRefreshListener() {
        binding.SwipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
    }

    private fun updateStatusAndText(status: Boolean) {
        viewModel.getStatus(status)
        binding.statusText.text = if (status) "ONLINE" else "OFFLINE"
    }

    private fun refreshData() {
        val statusRefreshed = NetworkChecker.isNetworkAvailable(requireContext())
        viewModel.refreshPage(statusRefreshed)
        binding.apply {
            SwipeRefreshLayout.isRefreshing = false
            statusText.text = if (statusRefreshed) "ONLINE" else "OFFLINE"
        }
    }

    private fun observeData(adapter: PagingDataAdapter<T, VH>) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dataFlow.collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
            }
        }
    }

    private fun observeLoadState(adapter: PagingDataAdapter<T, VH>) {
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.apply {
                    progressBar.isVisible = loadStates.refresh is LoadState.Loading
                    emptyStateText.isVisible = loadStates.refresh is LoadState.Error
                }
            }
        }
    }
}