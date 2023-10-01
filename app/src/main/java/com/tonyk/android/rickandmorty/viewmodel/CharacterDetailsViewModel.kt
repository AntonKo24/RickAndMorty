package com.tonyk.android.rickandmorty.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.tonyk.android.rickandmorty.data.repository.EpisodesRepository
import com.tonyk.android.rickandmorty.data.repository.LocationsRepository
import com.tonyk.android.rickandmorty.model.episode.EpisodeEntity
import com.tonyk.android.rickandmorty.model.location.LocationEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    private val episodesRepository: EpisodesRepository,
    private val locationRepository: LocationsRepository
) : BaseDetailViewModel<EpisodeEntity>() {

    var location: LocationEntity? = null

    var origin: LocationEntity? = null

    var test: LocationEntity? = null

    override fun loadData() {
        if (ids.isNotEmpty())
            viewModelScope.launch {
                episodesRepository.getEpisodeListById(ids, networkStatus)
                    .cachedIn(viewModelScope)
                    .collect { pagingData ->
                        _dataFlow.value = pagingData
                    }
            }
    }

    fun loadLocation(id: String) {
        if (id.isNotEmpty()) {
            viewModelScope.launch {
                val result = locationRepository.getLocationById(id, networkStatus)
                location = result
            }
        }
    }

    fun loadOrigin(id: String) {
        if (id.isNotEmpty()) {
            viewModelScope.launch {
                val result = locationRepository.getLocationById(id, networkStatus)
                origin = result
            }
        }
    }

    fun loadTest(id: String) {
        if (id.isNotEmpty())
            viewModelScope.launch {
                val result = locationRepository.getLocationById(id, networkStatus)
                test = result
            }
    }

}

