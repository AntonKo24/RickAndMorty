package com.tonyk.android.rickandmorty.ui.episode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.tonyk.android.rickandmorty.R
import com.tonyk.android.rickandmorty.databinding.FragmentEpisodeDetailsBinding
import com.tonyk.android.rickandmorty.model.character.CharacterEntity
import com.tonyk.android.rickandmorty.ui.base.BaseDetailsFragment
import com.tonyk.android.rickandmorty.ui.character.CharacterViewHolder
import com.tonyk.android.rickandmorty.ui.character.CharactersListAdapter
import com.tonyk.android.rickandmorty.viewmodel.episode.EpisodeDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EpisodeDetailsFragment : BaseDetailsFragment<CharacterEntity, CharacterViewHolder>() {
    private var _binding: FragmentEpisodeDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: EpisodeDetailsFragmentArgs by navArgs()

    override val viewModel: EpisodeDetailsViewModel by viewModels()

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

        initDetailsFragment(args.id)

    }

    override fun createAdapter(): PagingDataAdapter<CharacterEntity, CharacterViewHolder> {
        return CharactersListAdapter(
            onCharacterClicked = {
                findNavController().navigate(EpisodeDetailsFragmentDirections.toCharacterDetails(it.id))
            }
        )
    }

    override fun setupUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.episode.collect {
                binding.apply {
                    airDateEpisode.text = getString(R.string.episode_date, it.air_date)
                    episodeNameText.text = getString(R.string.episode_name, it.name)
                    episodeNumberText.text = getString(R.string.episode_number_fill, it.episode)
                }
            }
        }
        binding.apply {
            SwipeRefreshLayout.setOnRefreshListener {
                refreshFragmentData(args.id)
                SwipeRefreshLayout.isRefreshing = false
            }
            binding.backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun setupAdapter(adapter: PagingDataAdapter<CharacterEntity, CharacterViewHolder>) {
        binding.apply {
            episodeCharList.adapter = adapter
            episodeCharList.layoutManager = GridLayoutManager(context, 2)
        }
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.apply {
                    progressBar.isVisible =
                        loadStates.refresh is LoadState.Loading || loadStates.append is LoadState.Loading
                    emptyStateText.isVisible = loadStates.refresh is LoadState.Error
                    if (loadStates.append is LoadState.NotLoading && loadStates.append.endOfPaginationReached) {
                        emptyStateText.isVisible = adapter.itemCount < 1
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}