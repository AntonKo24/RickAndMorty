package com.tonyk.android.rickandmorty.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.tonyk.android.rickandmorty.model.episode.EpisodeEntity
import com.tonyk.android.rickandmorty.model.episode.EpisodeFilter
import com.tonyk.android.rickandmorty.repositoryimpl.EpisodesRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EpisodesViewModel @Inject constructor(
    private val repository: EpisodesRepositoryImpl
) : BaseListViewModel<EpisodeEntity, EpisodeFilter>(EpisodeFilter()) {

    override fun loadListData() {
        viewModelScope.launch {
            repository.getEpisodesList(_currentFilter, networkStatus)
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _data.value = pagingData
                }
        }
    }
}

