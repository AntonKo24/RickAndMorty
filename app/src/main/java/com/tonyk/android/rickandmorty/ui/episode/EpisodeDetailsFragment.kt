package com.tonyk.android.rickandmorty.ui.episode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.tonyk.android.rickandmorty.databinding.FragmentEpisodeDetailsBinding
import com.tonyk.android.rickandmorty.ui.character.CharactersListAdapter
import com.tonyk.android.rickandmorty.util.NetworkChecker
import com.tonyk.android.rickandmorty.viewmodel.episode.EpisodeDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EpisodeDetailsFragment : Fragment() {
    private var _binding: FragmentEpisodeDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: EpisodeDetailsFragmentArgs by navArgs()
    private val episodeDetailsViewModel : EpisodeDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEpisodeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeFragment()

        setupUi()

        setupAdapter()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeFragment() {
        val status = NetworkChecker.isNetworkAvailable(requireContext())
        episodeDetailsViewModel.initializeFragmentData(status, args.id)
    }

    private fun refreshData() {
        val statusRefreshed = NetworkChecker.isNetworkAvailable(requireContext())
        episodeDetailsViewModel.refreshPage(args.id, statusRefreshed)
        binding.SwipeRefreshLayout.isRefreshing = false
    }

    private fun setupUi() {
        viewLifecycleOwner.lifecycleScope.launch {
            episodeDetailsViewModel.episode.collect {
                binding.apply {
                    airDateEpisode.text = it.air_date
                    episodeNameText.text = it.name
                    episodeNumberText.text = it.episode
                    SwipeRefreshLayout.setOnRefreshListener {
                        refreshData()
                    }
                    backBtn.setOnClickListener {
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    private fun setupAdapter() {
        val adapter = CharactersListAdapter(
            onCharacterClicked = {
                findNavController().navigate(EpisodeDetailsFragmentDirections.toCharacterDetails(it.id))
            }
        )
        binding.apply {
            episodeCharList.adapter = adapter
            episodeCharList.layoutManager = GridLayoutManager(context, 2)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                episodeDetailsViewModel.dataFlow.collect { data ->
                    adapter.submitData(data)
                }
            }
        }
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.apply {
                    progressBar.isVisible = loadStates.refresh is LoadState.Loading
                    emptyStateText.isVisible = loadStates.refresh is LoadState.Error
                    if (loadStates.append is LoadState.NotLoading && loadStates.append.endOfPaginationReached) {
                        emptyStateText.isVisible = adapter.itemCount < 1
                    }
                }
            }
        }
    }
}