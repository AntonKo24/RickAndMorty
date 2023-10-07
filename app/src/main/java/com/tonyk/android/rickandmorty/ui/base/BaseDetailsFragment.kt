package com.tonyk.android.rickandmorty.ui.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tonyk.android.rickandmorty.util.NetworkChecker
import com.tonyk.android.rickandmorty.viewmodel.base.BaseDetailViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseDetailsFragment<T : Any, VH : RecyclerView.ViewHolder> : Fragment() {

    protected abstract val viewModel: BaseDetailViewModel<T>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = createAdapter()
        observeData(adapter)
        setupAdapter(adapter)
        observeErrorState()
        setupUI()
    }

    protected fun initDetailsFragment(id: Int) {
        val status = NetworkChecker.isNetworkAvailable(requireContext())
        viewModel.initializeDetailsFragment(status, id)
    }

    protected fun refreshFragmentData(id : Int) {
        val statusRefreshed = NetworkChecker.isNetworkAvailable(requireContext())
        viewModel.refreshDetailsFragment(statusRefreshed,id)
    }

    abstract fun createAdapter(): PagingDataAdapter<T, VH>
    abstract fun setupUI()
    abstract fun setupAdapter(adapter: PagingDataAdapter<T, VH>)
    
    private fun observeData(adapter: PagingDataAdapter<T, VH>) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dataFlow.collectLatest { pagingData ->
                    adapter.submitData(pagingData)
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