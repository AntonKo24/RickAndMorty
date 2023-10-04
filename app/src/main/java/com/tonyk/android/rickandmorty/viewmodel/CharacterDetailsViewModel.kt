package com.tonyk.android.rickandmorty.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.tonyk.android.rickandmorty.data.repository.EpisodesRepository
import com.tonyk.android.rickandmorty.data.repository.LocationsRepository
import com.tonyk.android.rickandmorty.model.episode.EpisodeEntity
import com.tonyk.android.rickandmorty.model.location.LocationEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    private val episodesRepository: EpisodesRepository,
    private val locationRepository: LocationsRepository
) : BaseDetailViewModel<EpisodeEntity>() {

    private var _location: MutableStateFlow<LocationEntity> = MutableStateFlow(
        LocationEntity(
            id = -1,
            name = "Unknown",
            type = "Unknown",
            dimension = "Unknown",
            emptyList()
        )
    )
    val location: StateFlow<LocationEntity> get() = _location

    private var _origin: MutableStateFlow<LocationEntity> = MutableStateFlow(
        LocationEntity(
            id = -1,
            name = "Unknown",
            type = "Unknown",
            dimension = "Unknown",
            emptyList()
        )
    )
    val origin: StateFlow<LocationEntity> get() = _origin

    override fun loadListData() {
        if (ids.isNotEmpty())
            viewModelScope.launch {
                episodesRepository.getEpisodeListById(ids, networkStatus)
                    .cachedIn(viewModelScope)
                    .collect { pagingData ->
                        _dataFlow.value = pagingData
                    }
            }
    }

    fun loadLocations(locationID: String?, originID: String?) {
        if (locationID != null) {
            viewModelScope.launch {
                try {
                    val result = locationRepository.getLocationById(locationID, networkStatus)
                    if (result != null) _location.value = result
                } catch (e: Exception) {

                }
            }
        }
            if (originID != null) {
                viewModelScope.launch {
                    try {
                        val result = locationRepository.getLocationById(originID, networkStatus)
                        if (result != null) _origin.value = result
                    } catch (e: Exception) {

                    }
                }

        }
    }
}

