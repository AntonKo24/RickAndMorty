package com.tonyk.android.rickandmorty.ui.episode

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tonyk.android.rickandmorty.databinding.FragmentEpisodesBinding
import com.tonyk.android.rickandmorty.ui.character.CharacterListFragmentDirections
import com.tonyk.android.rickandmorty.ui.character.CharactersListAdapter
import com.tonyk.android.rickandmorty.util.NetworkChecker
import com.tonyk.android.rickandmorty.viewmodel.CharactersViewModel
import com.tonyk.android.rickandmorty.viewmodel.EpisodesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EpisodeListFragment : Fragment() {
    private var _binding: FragmentEpisodesBinding? = null
    private val binding get() = _binding!!
    private val episodesViewModel : EpisodesViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEpisodesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("PAgingTest33333", "EPISODES NOW")
        val status = NetworkChecker.isNetworkAvailable(requireContext())
        episodesViewModel.getStatus(status)

        val adapter = EpisodeListAdapter(
            onEpisodeClicked = {
                    episode ->
                findNavController().navigate(EpisodeListFragmentDirections.toEpisodeDetail(episode))
            }
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                episodesViewModel.episodes.collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                    Log.d("PAgingTest33333", "EPISODES COLLECTIN")
                }
            }
        }
        binding.episodesRcv.layoutManager = LinearLayoutManager(context)
        binding.episodesRcv.adapter = adapter

        binding.episodeSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    episodesViewModel.applyFilter(query)
                    Log.d("PAgingTest33333", "EPISODES COLLECTIN")
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                }
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}