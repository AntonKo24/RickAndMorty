package com.tonyk.android.rickandmorty.ui.location

import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import com.tonyk.android.rickandmorty.model.location.LocationEntity
import com.tonyk.android.rickandmorty.ui.BaseListFragment
import com.tonyk.android.rickandmorty.ui.episode.EpisodeListFragmentDirections
import com.tonyk.android.rickandmorty.util.NetworkChecker
import com.tonyk.android.rickandmorty.viewmodel.LocationsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationListFragment : BaseListFragment<LocationEntity, LocationViewHolder>() {

    private val locationsViewModel: LocationsViewModel by activityViewModels()

    override fun checkStatus(status: Boolean) {
        locationsViewModel.getStatus(status)
        if (status) binding.statusText.text = "ONLINE"
        else binding.statusText.text = "OFFLINE"
    }

    override suspend fun observeLoadState(adapter: PagingDataAdapter<LocationEntity, LocationViewHolder>) {
        adapter.loadStateFlow.collectLatest { loadStates ->
            binding.progressBar.isVisible = loadStates.refresh is LoadState.Loading
            binding.emptyStateText.isVisible = loadStates.refresh is LoadState.Error
        }
    }

    override fun createAdapter(): PagingDataAdapter<LocationEntity, LocationViewHolder> {
        return LocationListAdapter(
            onLocationClicked = { location ->
                findNavController().navigate(
                    LocationListFragmentDirections.toLocationDetails(
                        location
                    )
                )
            }
        )
    }

    override fun collectData(adapter: PagingDataAdapter<LocationEntity, LocationViewHolder>) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                locationsViewModel.dataFlow.collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
            }
        }
    }

    override fun navigateToFilterFragment() {
        findNavController().navigate(LocationListFragmentDirections.toEocationsFilterFragment())
    }

    override fun refreshData() {
        val statusRefreshed = NetworkChecker.isNetworkAvailable(requireContext())
        locationsViewModel.refreshPage(statusRefreshed)
        binding.SwipeRefreshLayout.isRefreshing = false
        Toast.makeText(
            requireContext(),
            "${NetworkChecker.isNetworkAvailable(requireContext())}",
            Toast.LENGTH_LONG
        ).show()
        if (statusRefreshed) binding.statusText.text = "ONLINE"
        else binding.statusText.text = "OFFLINE"
    }
}