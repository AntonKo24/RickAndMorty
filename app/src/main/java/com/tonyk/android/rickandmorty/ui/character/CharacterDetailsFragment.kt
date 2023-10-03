package com.tonyk.android.rickandmorty.ui.character

import android.os.Bundle
import android.util.Log
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

        val ld = mutableListOf<String>()
        for (url in args.character.episode) {
            val id = url.substringAfterLast("/")
            ld.add(id)
        }
        if (ld.isEmpty()) binding.progressBar.isVisible = false
        else characterDetailsViewModel.getStatus(
            NetworkChecker.isNetworkAvailable(requireContext()),
            ld
        )

        if (args.character.location.url.isNotEmpty()) {
            val locationId = args.character.location.url.substringAfterLast("/")
            characterDetailsViewModel.loadLocation(locationId)
        }

        if (args.character.origin.url.isNotEmpty()) {
            val originID = args.character.origin.url.substringAfterLast("/")
            characterDetailsViewModel.loadOrigin(originID)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        viewLifecycleOwner.lifecycleScope.launch {
            characterDetailsViewModel.location.collect { location ->
                Log.d("asdasdasd2", "$location")
                withContext(Dispatchers.Main) {
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
        }
        viewLifecycleOwner.lifecycleScope.launch {
            characterDetailsViewModel.origin.collect { origin ->
                Log.d("asdasdasd2", "$origin")
                withContext(Dispatchers.Main) {
                    binding.originLocationName.setOnClickListener {
                        if (origin.id != -1) {

                            Log.d("asdasdasd2sss", "${origin.residents}")
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


        binding.apply {
            charDetailsName.text = args.character.name
            characterPhoto.load(args.character.image)
            binding.originLocationName.text = args.character.origin.name
            binding.charLocationName.text = args.character.location.name


            val adapter = EpisodeListAdapter(
                onEpisodeClicked = {
                    findNavController().navigate(
                        CharacterDetailsFragmentDirections.toEpisodeDetailsFragment(
                            it
                        )
                    )
                }
            )
            charEpisodesList.layoutManager = LinearLayoutManager(context)
            charEpisodesList.adapter = adapter
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    characterDetailsViewModel.dataFlow.collect() { data ->
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}