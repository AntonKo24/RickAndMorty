package com.tonyk.android.rickandmorty.ui.episode

import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import com.tonyk.android.rickandmorty.model.character.CharacterEntity
import com.tonyk.android.rickandmorty.model.episode.EpisodeEntity
import com.tonyk.android.rickandmorty.ui.BaseListFragment
import com.tonyk.android.rickandmorty.ui.character.CharacterViewHolder
import com.tonyk.android.rickandmorty.util.NetworkChecker
import com.tonyk.android.rickandmorty.viewmodel.EpisodesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EpisodeListFragment : BaseListFragment<EpisodeEntity, EpisodeViewHolder>() {

    private val episodesViewModel: EpisodesViewModel by activityViewModels()

    override fun checkStatus(status: Boolean) {
        episodesViewModel.getStatus(status)
        if (status) binding.statusText.text = "ONLINE"
        else binding.statusText.text = "OFFLINE"
    }

    override suspend fun observeLoadState(adapter: PagingDataAdapter<EpisodeEntity, EpisodeViewHolder>) {
        adapter.loadStateFlow.collectLatest { loadStates ->
            binding.progressBar.isVisible = loadStates.refresh is LoadState.Loading
            binding.emptyStateText.isVisible = loadStates.refresh is LoadState.Error
        }
    }

    override fun createAdapter(): PagingDataAdapter<EpisodeEntity, EpisodeViewHolder> {
        return EpisodeListAdapter(
            onEpisodeClicked = { episode ->
                findNavController().navigate(
                    EpisodeListFragmentDirections.toEpisodeDetail(
                        episode
                    )
                )
            }
        )
    }

    override fun collectData(adapter: PagingDataAdapter<EpisodeEntity, EpisodeViewHolder>) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                episodesViewModel.dataFlow.collectLatest { pagingData ->
                    adapter.submitData(pagingData)

                }
            }
        }
    }

    override fun navigateToFilterFragment() {
        findNavController().navigate(EpisodeListFragmentDirections.toEpisodesFilterFragment())
    }

    override fun refreshData() {
        val statusRefreshed = NetworkChecker.isNetworkAvailable(requireContext())
        episodesViewModel.refreshPage(statusRefreshed)
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
