package com.tonyk.android.rickandmorty.ui.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.tonyk.android.rickandmorty.databinding.FragmentMainListBinding
import com.tonyk.android.rickandmorty.model.character.CharacterEntity
import com.tonyk.android.rickandmorty.ui.BaseListFragment
import com.tonyk.android.rickandmorty.util.NetworkChecker
import com.tonyk.android.rickandmorty.viewmodel.CharactersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharacterListFragment : BaseListFragment<CharacterEntity, CharacterViewHolder>() {

    private val charactersViewModel: CharactersViewModel by activityViewModels()

    override fun checkStatus(status: Boolean) {
        charactersViewModel.getStatus(status)
        if (status) binding.statusText.text = "ONLINE"
        else binding.statusText.text = "OFFLINE"
    }
    override fun createAdapter(): PagingDataAdapter<CharacterEntity, CharacterViewHolder> {
        return CharactersListAdapter(
            onCharacterClicked = { character ->
                findNavController().navigate(
                    CharacterListFragmentDirections.toCharacterDetail(
                        character
                    )
                )
            }
        )
    }
    override fun collectData(adapter: PagingDataAdapter<CharacterEntity, CharacterViewHolder>) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                charactersViewModel.dataFlow.collectLatest { pagingData ->
                    adapter.submitData(pagingData)

                }
            }
        }
    }

    override fun navigateToFilterFragment() {
        findNavController().navigate(CharacterListFragmentDirections.toCharactersFilterFragment())
    }

    override fun refreshData() {
        val statusRefreshed = NetworkChecker.isNetworkAvailable(requireContext())
        charactersViewModel.refreshPage(statusRefreshed)
        binding.SwipeRefreshLayout.isRefreshing = false
        Toast.makeText(
            requireContext(),
            "${NetworkChecker.isNetworkAvailable(requireContext())}",
            Toast.LENGTH_LONG
        ).show()
        if (statusRefreshed) binding.statusText.text = "ONLINE"
        else binding.statusText.text = "OFFLINE"
    }

    override suspend fun observeLoadState(adapter: PagingDataAdapter<CharacterEntity, CharacterViewHolder>) {
        adapter.loadStateFlow.collectLatest { loadStates ->
            binding.progressBar.isVisible = loadStates.refresh is LoadState.Loading
            binding.emptyStateText.isVisible = loadStates.refresh is LoadState.Error
        }
    }
}
