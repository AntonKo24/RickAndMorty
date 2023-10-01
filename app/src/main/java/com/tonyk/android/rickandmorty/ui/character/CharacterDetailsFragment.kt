package com.tonyk.android.rickandmorty.ui.character

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import com.tonyk.android.rickandmorty.model.location.LocationEntity
import com.tonyk.android.rickandmorty.ui.episode.EpisodeListAdapter
import com.tonyk.android.rickandmorty.util.NetworkChecker
import com.tonyk.android.rickandmorty.viewmodel.CharacterDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
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

        val locationId = args.character.location.url.substringAfterLast("/")
        val originID = args.character.origin.url.substringAfterLast("/")

        characterDetailsViewModel.loadLocation(locationId)
        characterDetailsViewModel.loadOrigin(originID)

        val res = characterDetailsViewModel.loadTest(locationId)
        Log.d("ASDASdAsd", "${characterDetailsViewModel.test}")


        binding.apply {
            charDetailsName.text = args.character.name
            characterPhoto.load(args.character.image)
            charLocationName.text = args.character.location.name
            originLocationName.text = args.character.origin.name


            characterPhoto.setOnClickListener {

            }


                    charLocationName.setOnClickListener {
                        val result = characterDetailsViewModel.location
                        if (result != null) {
                        findNavController().navigate(CharacterDetailsFragmentDirections.toLocationDetails(result))
                        }
                    }




                originLocationName.setOnClickListener {
                    val result = characterDetailsViewModel.origin
                    if (result != null) {
                    findNavController().navigate(CharacterDetailsFragmentDirections.toLocationDetails(result ))
                    }
                }




            charEpisodesList.layoutManager = LinearLayoutManager(context)

            val adapter = EpisodeListAdapter(
                onEpisodeClicked = {
                    findNavController().navigate(
                        CharacterDetailsFragmentDirections.toEpisodeDetailsFragment(
                            it
                        )
                    )
                }
            )

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
                    binding.progressBar.isVisible = loadStates.refresh is LoadState.Loading
                    binding.emptyStateText.isVisible = loadStates.refresh is LoadState.Error
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}