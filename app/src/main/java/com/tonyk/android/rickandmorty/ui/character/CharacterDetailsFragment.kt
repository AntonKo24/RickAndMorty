package com.tonyk.android.rickandmorty.ui.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.tonyk.android.rickandmorty.viewmodel.CharacterDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        setupUi()
        setupListData(args.character.episode)
        setupAdapter()
        setupLocationsData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun refreshData() {
        val statusRefreshed = NetworkChecker.isNetworkAvailable(requireContext())
        characterDetailsViewModel.refreshPage(statusRefreshed)
        binding.SwipeRefreshLayout.isRefreshing = false
    }

    private fun setupAdapter() {
        val adapter = EpisodeListAdapter(
            onEpisodeClicked = {
                findNavController().navigate(
                    CharacterDetailsFragmentDirections.toEpisodeDetailsFragment(
                        it
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
        binding.apply {
            charDetailsName.text = args.character.name
            characterPhoto.load(args.character.image)
            originLocationName.text = getString(R.string.origin_location, args.character.origin.name)
            charLocationName.text = getString(R.string.location, args.character.location.name)
            speciesText.text = getString(R.string.Species,args.character.species)
            genderText.text = getString(R.string.Gender, args.character.gender)
            if (args.character.type.isNotEmpty()) typeText.text = getString(R.string.Type, args.character.type)
            charStatusText.text = getString(R.string.Status, args.character.status)

            SwipeRefreshLayout.setOnRefreshListener {
                refreshData()
            }
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
        }

    }

    private fun setupLocationsData() {
        val locationId = args.character.location.url.substringAfterLast("/")
        val originID = args.character.origin.url.substringAfterLast("/")
        characterDetailsViewModel.loadLocations(locationId, originID)
        viewLifecycleOwner.lifecycleScope.launch {
            characterDetailsViewModel.location.collect { location ->
                binding.charLocationName.setOnClickListener {
                    if (location.id != -1) {
                        findNavController().navigate(
                            CharacterDetailsFragmentDirections.toLocationDetails(
                                location
                            )
                        )
                    } else Toast.makeText(
                        requireContext(),
                        "Can't load Data about Location",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            characterDetailsViewModel.origin.collect { origin ->
                withContext(Dispatchers.Main) {
                    binding.originLocationName.setOnClickListener {
                        if (origin.id != -1) {
                            findNavController().navigate(
                                CharacterDetailsFragmentDirections.toLocationDetails(
                                    origin
                                )
                            )
                        } else Toast.makeText(
                            requireContext(),
                            "Can't load Data about Location",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
    private fun setupListData(urls : List<String>) {
        val idList = mutableListOf<String>()
        for (url in urls) {
            val id = url.substringAfterLast("/")
            if (id!="") idList.add(id)
        }
        if (idList.isEmpty()) binding.progressBar.isVisible = false
        else characterDetailsViewModel.getStatus(
            NetworkChecker.isNetworkAvailable(requireContext()),
            idList
        )
    }
}