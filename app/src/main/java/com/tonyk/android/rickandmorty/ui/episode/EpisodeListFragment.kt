package com.tonyk.android.rickandmorty.ui.episode

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingDataAdapter
import com.tonyk.android.rickandmorty.model.episode.EpisodeEntity
import com.tonyk.android.rickandmorty.ui.BaseListFragment
import com.tonyk.android.rickandmorty.viewmodel.EpisodesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EpisodeListFragment : BaseListFragment<EpisodeEntity, EpisodeViewHolder>() {

    override val viewModel: EpisodesViewModel by activityViewModels()

    override fun createAdapter(): PagingDataAdapter<EpisodeEntity, EpisodeViewHolder> {
        return EpisodeListAdapter(
            onEpisodeClicked = { episode ->
                findNavController().navigate(
                    EpisodeListFragmentDirections.toEpisodeDetail(
                        episode
                    )
                )
            }
        )
    }

    override fun navigateToFilterFragment() {
        findNavController().navigate(EpisodeListFragmentDirections.toEpisodesFilterFragment())
    }

}
