package com.tonyk.android.rickandmorty.viewmodel.episode

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.tonyk.android.rickandmorty.data.repository.EpisodesRepository
import com.tonyk.android.rickandmorty.model.episode.EpisodeEntity
import com.tonyk.android.rickandmorty.model.episode.EpisodeFilter
import com.tonyk.android.rickandmorty.viewmodel.base.BaseListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EpisodesListViewModel @Inject constructor(
    private val repository: EpisodesRepository
) : BaseListViewModel<EpisodeEntity, EpisodeFilter>(EpisodeFilter()) {

    override fun loadMainListData() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    repository.getEpisodesListWithFilters(_currentFilter, networkStatus)
                }
                    .cachedIn(viewModelScope)
                    .collect { pagingData ->
                        _dataFlow.value = pagingData
                    }
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }
}