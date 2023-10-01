package com.tonyk.android.rickandmorty.ui.episode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.tonyk.android.rickandmorty.databinding.FragmentEpisodesFilterBinding
import com.tonyk.android.rickandmorty.model.episode.EpisodeFilter
import com.tonyk.android.rickandmorty.viewmodel.EpisodesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EpisodesFilterFragment : Fragment() {
    private var _binding: FragmentEpisodesFilterBinding? = null
    private val binding get() = _binding!!
    private val episodesViewModel: EpisodesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEpisodesFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentFilter = episodesViewModel.getCurrentFilter()

        binding.episodeNamePicker.setText(currentFilter.name)
        binding.episodeNumberPicker.setText(currentFilter.episode)

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.clearBtn.setOnClickListener {
            binding.episodeNamePicker.text.clear()
            binding.episodeNumberPicker.text.clear()
        }

        binding.buttonApply.setOnClickListener {

            val episodeName = binding.episodeNamePicker.text.toString().takeIf { it.isNotEmpty() }
            val episodeNumber =
                binding.episodeNumberPicker.text.toString().takeIf { it.isNotEmpty() }

            val episodeFilter = EpisodeFilter(
                name = episodeName,
                episode = episodeNumber
            )

            episodesViewModel.applyFilter(episodeFilter)
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}