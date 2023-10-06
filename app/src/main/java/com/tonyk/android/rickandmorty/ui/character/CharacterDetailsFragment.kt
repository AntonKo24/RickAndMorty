package com.tonyk.android.rickandmorty.ui.character

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
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.tonyk.android.rickandmorty.R
import com.tonyk.android.rickandmorty.databinding.FragmentCharacterDetailsBinding
import com.tonyk.android.rickandmorty.ui.episode.EpisodeListAdapter
import com.tonyk.android.rickandmorty.util.NetworkChecker
import com.tonyk.android.rickandmorty.viewmodel.character.CharacterDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharacterDetailsFragment : Fragment() {
    private var _binding: FragmentCharacterDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: CharacterDetailsFragmentArgs by navArgs()
    private val characterDetailsViewModel: CharacterDetailsViewModel by viewModels()

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

        initializeFragment()

        setupUi()

        setupAdapter()

        setupLocationsData()

        binding.apply {
            SwipeRefreshLayout.setOnRefreshListener {
                refreshData()
            }
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeFragment() {
        val status = NetworkChecker.isNetworkAvailable(requireContext())
        characterDetailsViewModel.initializeFragmentData(status, args.id)
    }

    private fun refreshData() {
        val statusRefreshed = NetworkChecker.isNetworkAvailable(requireContext())
        characterDetailsViewModel.refreshPage(args.id, statusRefreshed )
        binding.SwipeRefreshLayout.isRefreshing = false
    }

    private fun setupAdapter() {
        val adapter = EpisodeListAdapter(
            onEpisodeClicked = {
                findNavController().navigate(
                    CharacterDetailsFragmentDirections.toEpisodeDetailsFragment(
                        it.id
                    )
                )
            }
        )
        binding.apply {
            charEpisodesList.adapter = adapter
            charEpisodesList.layoutManager = LinearLayoutManager(context)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                characterDetailsViewModel.dataFlow.collect { data ->
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

    private fun setupUi() {
        viewLifecycleOwner.lifecycleScope.launch {
            characterDetailsViewModel.character.collect {
                binding.apply {
                    charDetailsName.text = it.name
                    characterPhoto.load(it.image) {
                        crossfade(true)
                        placeholder(R.drawable.ic_loading)
                        error(R.drawable.error_pic)
                    }
                    originLocationName.text =
                        getString(R.string.origin_location, it.origin.name)
                    charLocationName.text = getString(R.string.location, it.location.name)
                    speciesText.text = getString(R.string.Species, it.species)
                    genderText.text = getString(R.string.Gender, it.gender)
                    if (it.type.isNotEmpty()) typeText.text =
                        getString(R.string.Type, it.type)
                    charStatusText.text = getString(R.string.Status, it.status)

                }
            }
        }
    }

    private fun setupLocationsData() {
        viewLifecycleOwner.lifecycleScope.launch {
            characterDetailsViewModel.location.collect { location ->
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
            characterDetailsViewModel.origin.collect { origin ->
                if (origin.id != -1) {
                    binding.apply {
                        originLocationName.text = getString(R.string.origin_location_loaded, origin.name)
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