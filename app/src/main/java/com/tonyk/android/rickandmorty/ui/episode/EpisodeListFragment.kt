package com.tonyk.android.rickandmorty.ui.episode

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingDataAdapter
import com.tonyk.android.rickandmorty.model.episode.EpisodeEntity
import com.tonyk.android.rickandmorty.ui.base.BaseListFragment
import com.tonyk.android.rickandmorty.viewmodel.episode.EpisodesListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EpisodeListFragment : BaseListFragment<EpisodeEntity, EpisodeViewHolder>() {

    override val viewModel: EpisodesListViewModel by activityViewModels()

    override fun createAdapter(): PagingDataAdapter<EpisodeEntity, EpisodeViewHolder> {
        return EpisodeListAdapter(
            onEpisodeClicked = { episode ->
                findNavController().navigate(
                    EpisodeListFragmentDirections.toEpisodeDetail(
                        episode.id
                    )
                )
            }
        )
    }

    override fun navigateToFilterFragment() {
        findNavController().navigate(EpisodeListFragmentDirections.toEpisodesFilterFragment())
    }

}
