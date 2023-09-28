package com.tonyk.android.rickandmorty.ui.episode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.tonyk.android.rickandmorty.databinding.FragmentEpisodeDetailsBinding
import com.tonyk.android.rickandmorty.ui.character.CharacterDetailsFragmentArgs
import com.tonyk.android.rickandmorty.ui.character.CharacterListFragmentDirections
import com.tonyk.android.rickandmorty.ui.character.CharactersListAdapter
import com.tonyk.android.rickandmorty.ui.character.EpListCharAdapter
import com.tonyk.android.rickandmorty.util.NetworkChecker
import com.tonyk.android.rickandmorty.viewmodel.CharacterDetailsViewModel
import com.tonyk.android.rickandmorty.viewmodel.EpisodeDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EpisodeDetailsFragment : Fragment() {
    private var _binding: FragmentEpisodeDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: EpisodeDetailsFragmentArgs by navArgs()
    private val detVM : EpisodeDetailViewModel by viewModels()

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

        val urls = args.episode.characters
        val ld = mutableListOf<String>()
        for (url in urls) {
            val parts = url.split('/')
            val lastDigit = parts.last()
            ld.add(lastDigit)
        }




        detVM.getStatus(NetworkChecker.isNetworkAvailable(requireContext()), ld)

        binding.episodeCharList.layoutManager = LinearLayoutManager(context)

        val adapter = CharactersListAdapter(
            onCharacterClicked = {
                    it ->
                findNavController().navigate(EpisodeDetailsFragmentDirections.toCharacterDetails(it))
            }
        )

        binding.episodeCharList.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                detVM.characters.collect() { data ->
                    adapter.submitData(data)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}