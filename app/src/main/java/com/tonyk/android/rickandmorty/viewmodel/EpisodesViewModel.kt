package com.tonyk.android.rickandmorty.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tonyk.android.rickandmorty.model.character.CharacterFilter
import com.tonyk.android.rickandmorty.model.episode.EpisodeEntity
import com.tonyk.android.rickandmorty.model.episode.EpisodeFilter
import com.tonyk.android.rickandmorty.repositoryimpl.EpisodesRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EpisodesViewModel @Inject constructor(
    private val repository: EpisodesRepositoryImpl
) : ViewModel() {
    private val _episodes = MutableStateFlow<PagingData<EpisodeEntity>>(PagingData.empty())
    val episodes: StateFlow<PagingData<EpisodeEntity>> = _episodes.asStateFlow()

    private var _currentFilter: EpisodeFilter = EpisodeFilter()
    val currentFilter get() = _currentFilter

    private var networkStatus: Boolean = false

    fun getStatus(status: Boolean) {
            networkStatus = status
            loadEpisodes()
    }

    fun refreshPage(status: Boolean) {
        if (status != networkStatus) {
            networkStatus = status
            viewModelScope.coroutineContext.cancelChildren()
            loadEpisodes()
        }
    }

    private fun loadEpisodes() {
        viewModelScope.launch {
                repository.getEpisodeList(currentFilter, networkStatus)
                    .cachedIn(viewModelScope)
                    .collect { pagingData ->
                        _episodes.value = pagingData
                    }
              }
    }

    fun applyFilter(filter: EpisodeFilter) {
        _currentFilter = filter
        loadEpisodes()
    }
}
