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
import com.tonyk.android.rickandmorty.viewmodel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EpisodeDetailsFragment : Fragment() {
    private var _binding: FragmentEpisodeDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: EpisodeDetailsFragmentArgs by navArgs()
    private val detailsViewModel : DetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEpisodeDetailsBinding.inflate(inflater, container, false)

        val urls = args.episode.characters
        val ld = mutableListOf<String>()
        for (url in urls) {
            val parts = url.split('/')
            val lastDigit = parts.last()
            ld.add(lastDigit)
        }
        if (ld.isEmpty()) binding.progressBar.isVisible = false
        else detailsViewModel.getStatus(NetworkChecker.isNetworkAvailable(requireContext()), ld)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.airDateEpisode.text = args.episode.air_date
        binding.episodeNameText.text = args.episode.name
        binding.episodeNumberText.text = args.episode.episode




        binding.episodeCharList.layoutManager = GridLayoutManager(context, 2)

        val adapter = CharactersListAdapter(
            onCharacterClicked = {
                    it ->
                findNavController().navigate(EpisodeDetailsFragmentDirections.toCharacterDetails(it))
            }
        )

        binding.episodeCharList.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailsViewModel.dataFlow.collect() { data ->
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}