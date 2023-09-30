package com.tonyk.android.rickandmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tonyk.android.rickandmorty.model.character.CharacterEntity
import com.tonyk.android.rickandmorty.model.episode.EpisodeEntity
import com.tonyk.android.rickandmorty.model.location.LocationEntity
import com.tonyk.android.rickandmorty.repositoryimpl.EpisodesRepositoryImpl
import com.tonyk.android.rickandmorty.repositoryimpl.LocationsRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    private val episodeRepository: EpisodesRepositoryImpl,
    private val locationsRepository: LocationsRepositoryImpl
) : ViewModel() {
    private var networkStatus: Boolean = false

    private val _episodes = MutableStateFlow<PagingData<EpisodeEntity>>(PagingData.empty())
    val episodes: StateFlow<PagingData<EpisodeEntity>> = _episodes.asStateFlow()

    private var episodesIDs: List<String> = emptyList()
    fun getStatus(status: Boolean, ids: List<String>) {
        networkStatus = status
        episodesIDs = ids
        loadEpisodes()
    }

    private fun loadEpisodes() {
        viewModelScope.launch {
                episodeRepository.getEpisodeListById(episodesIDs, networkStatus)
                    .cachedIn(viewModelScope)
                    .collect { pagingData ->
                        _episodes.value = pagingData
                    }
        }
    }

    suspend fun loadLocation(id: String): LocationEntity {
        return locationsRepository.getLocationById(id, networkStatus)
    }
}