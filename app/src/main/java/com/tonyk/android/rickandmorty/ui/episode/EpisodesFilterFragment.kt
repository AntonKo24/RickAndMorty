package com.tonyk.android.rickandmorty.ui.episode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.tonyk.android.rickandmorty.databinding.FragmentEpisodesFilterBinding
import com.tonyk.android.rickandmorty.model.episode.EpisodeEntity
import com.tonyk.android.rickandmorty.model.episode.EpisodeFilter
import com.tonyk.android.rickandmorty.ui.base.BaseFragment
import com.tonyk.android.rickandmorty.viewmodel.episode.EpisodesListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EpisodesFilterFragment : BaseFragment<EpisodeEntity>() {
    private var _binding: FragmentEpisodesFilterBinding? = null
    private val binding get() = _binding!!

    override val viewModel: EpisodesListViewModel by activityViewModels()

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

        setupUi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUi() {
        val currentFilter = viewModel.getCurrentFilter()

        binding.apply {
            episodeNamePicker.setText(currentFilter.name)
            episodeNumberPicker.setText(currentFilter.episode)

            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }

            clearBtn.setOnClickListener {
                clearFields()
            }

            buttonApply.setOnClickListener {
                applyFilter()
            }
        }
    }

    private fun clearFields() {
        binding.episodeNamePicker.text.clear()
        binding.episodeNumberPicker.text.clear()
    }

    private fun applyFilter() {
        val episodeFilter = createEpisodeFilter()
        viewModel.applyFilter(episodeFilter)
        findNavController().popBackStack()
    }

    private fun createEpisodeFilter(): EpisodeFilter {
        val episodeName = binding.episodeNamePicker.text.toString().takeIf { it.isNotEmpty() }
        val episodeNumber = binding.episodeNumberPicker.text.toString().takeIf { it.isNotEmpty() }
        return EpisodeFilter(
            name = episodeName,
            episode = episodeNumber
        )
    }
}
