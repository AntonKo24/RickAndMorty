package com.tonyk.android.rickandmorty.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tonyk.android.rickandmorty.R
import com.tonyk.android.rickandmorty.databinding.FragmentMainListBinding
import com.tonyk.android.rickandmorty.util.NetworkChecker
import com.tonyk.android.rickandmorty.viewmodel.base.BaseListViewModel
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

        initListFragment()
        val adapter = createAdapter()
        observeData(adapter)
        setupAdapter(adapter)
        observeErrorState()

        observeLoadState(adapter)
        setupRefreshListener()

        binding.filters.setOnClickListener {
            navigateToFilterFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun createAdapter(): PagingDataAdapter<T, VH>
    abstract fun navigateToFilterFragment()

    private fun setupAdapter(adapter: PagingDataAdapter<T, VH>) {
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

    private fun initListFragment() {
        val status = NetworkChecker.isNetworkAvailable(requireContext())
        viewModel.initializeListFragment(status)
        if (status) {
            binding.statusText.text =  getString(R.string.online)
            binding.icStatus.load(R.drawable.ic_online)
        }
        else {
            binding.icStatus.load(R.drawable.ic_offline)
            binding.statusText.text =  getString(R.string.offline)
        }
    }

    private fun refreshData() {
        val statusRefreshed = NetworkChecker.isNetworkAvailable(requireContext())
        viewModel.refreshListFragment(statusRefreshed)
        binding.apply {
            SwipeRefreshLayout.isRefreshing = false
            if (statusRefreshed) {
                binding.statusText.text =  getString(R.string.online)
                binding.icStatus.load(R.drawable.ic_online)
            }
            else {
                binding.icStatus.load(R.drawable.ic_offline)
                binding.statusText.text =  getString(R.string.offline)
            }
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
                    progressBar.isVisible = loadStates.refresh is LoadState.Loading || loadStates.append is LoadState.Loading
                    emptyStateText.isVisible = loadStates.refresh is LoadState.Error
                    if (loadStates.append is LoadState.NotLoading && loadStates.append.endOfPaginationReached) {
                        emptyStateText.isVisible = adapter.itemCount < 1
                    }
                }
            }
        }
    }
    private fun observeErrorState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorState.collect { error ->
                    Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}