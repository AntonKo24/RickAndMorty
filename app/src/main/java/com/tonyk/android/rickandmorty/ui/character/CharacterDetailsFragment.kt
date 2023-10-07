package com.tonyk.android.rickandmorty.ui.character

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
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.tonyk.android.rickandmorty.R
import com.tonyk.android.rickandmorty.databinding.FragmentCharacterDetailsBinding
import com.tonyk.android.rickandmorty.model.episode.EpisodeEntity
import com.tonyk.android.rickandmorty.ui.base.BaseDetailsFragment
import com.tonyk.android.rickandmorty.ui.episode.EpisodeListAdapter
import com.tonyk.android.rickandmorty.ui.episode.EpisodeViewHolder
import com.tonyk.android.rickandmorty.viewmodel.character.CharacterDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharacterDetailsFragment : BaseDetailsFragment<EpisodeEntity, EpisodeViewHolder>() {
    private var _binding: FragmentCharacterDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: CharacterDetailsFragmentArgs by navArgs()

    override val viewModel: CharacterDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCharacterDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDetailsFragment(args.id)
        setupLocationsData()
    }

    override fun createAdapter(): PagingDataAdapter<EpisodeEntity, EpisodeViewHolder> {
        return EpisodeListAdapter(
            onEpisodeClicked = { episode ->
                findNavController().navigate(
                    CharacterDetailsFragmentDirections.toEpisodeDetailsFragment(episode.id)

                )

            }
        )
    }

    override fun setupUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.character.collect {character ->
                binding.apply {
                    charDetailsName.text = character.name
                    characterPhoto.load(character.image) {
                        crossfade(true)
                        placeholder(R.drawable.ic_loading)
                        error(R.drawable.error_pic)
                    }
                    characterPhoto.setOnClickListener {
                        characterPhoto.load(character.image) {
                            crossfade(true)
                            placeholder(R.drawable.ic_loading)
                            error(R.drawable.error_pic)
                        }
                    }
                    originLocationName.text =
                        getString(R.string.origin_location, character.origin.name)
                    charLocationName.text = getString(R.string.location, character.location.name)
                    speciesText.text = getString(R.string.Species, character.species)
                    genderText.text = getString(R.string.Gender, character.gender)
                    if (character.type.isNotEmpty()) typeText.text =
                        getString(R.string.Type, character.type)
                    charStatusText.text = getString(R.string.Status, character.status)

                }
            }
        }
        binding.apply {
            SwipeRefreshLayout.setOnRefreshListener {
                refreshFragmentData(args.id)
                SwipeRefreshLayout.isRefreshing = false
            }
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
        }

    }

    override fun setupAdapter(adapter: PagingDataAdapter<EpisodeEntity, EpisodeViewHolder>) {
        binding.apply {
            charEpisodesList.adapter = adapter
            charEpisodesList.layoutManager = LinearLayoutManager(context)
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


    private fun setupLocationsData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.location.collect { location ->
                if (location.id != -1) {
                    binding.apply {
                        charLocationName.text = getString(R.string.location_loaded, location.name)
                        binding.charLocationName.setOnClickListener {
                            findNavController().navigate(
                                CharacterDetailsFragmentDirections.toLocationDetails(
                                    location.id
                                )
                            )
                        }
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.origin.collect { origin ->
                if (origin.id != -1) {
                    binding.apply {
                        originLocationName.text =
                            getString(R.string.origin_location_loaded, origin.name)
                        binding.originLocationName.setOnClickListener {
                            findNavController().navigate(
                                CharacterDetailsFragmentDirections.toLocationDetails(
                                    origin.id
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}