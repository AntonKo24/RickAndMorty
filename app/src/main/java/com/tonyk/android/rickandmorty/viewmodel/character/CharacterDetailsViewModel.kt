package com.tonyk.android.rickandmorty.viewmodel.character

import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.tonyk.android.rickandmorty.data.repository.CharactersRepository
import com.tonyk.android.rickandmorty.data.repository.EpisodesRepository
import com.tonyk.android.rickandmorty.data.repository.LocationsRepository
import com.tonyk.android.rickandmorty.model.character.CharacterEntity
import com.tonyk.android.rickandmorty.model.character.CharacterLocation
import com.tonyk.android.rickandmorty.model.character.CharacterOrigin
import com.tonyk.android.rickandmorty.model.episode.EpisodeEntity
import com.tonyk.android.rickandmorty.model.location.LocationEntity
import com.tonyk.android.rickandmorty.util.NetworkChecker
import com.tonyk.android.rickandmorty.viewmodel.BaseDetailViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    private val episodesRepository: EpisodesRepository,
    private val locationRepository: LocationsRepository,
    private val charactersRepository: CharactersRepository
) : BaseDetailViewModel<EpisodeEntity>() {

    val character : StateFlow<CharacterEntity> get() = _character
    val location: StateFlow<LocationEntity> get() = _location
    val origin: StateFlow<LocationEntity> get() = _origin

    override fun loadEntityData(id: Int) {

        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO)  { charactersRepository.getCharacterByID(id, networkStatus) }
                _character.value = result


                val urls = result.episode
                val idList = mutableListOf<String>()
                for (url in urls) {
                    val residentID = url.substringAfterLast("/")
                    idList.add(residentID)
                }
                withContext(Dispatchers.IO)  { loadListData(idList) }

                val locationId = result.location.url.substringAfterLast("/")
                val originID = result.origin.url.substringAfterLast("/")
                withContext(Dispatchers.IO) { loadLocations(locationId, originID) }

            } catch (e: Exception) {

            }
        }
    }

    override fun loadListData(ids : List<String>) {
        if (ids.isNotEmpty())
            viewModelScope.launch {
                episodesRepository.getEpisodeListById(ids, networkStatus)
                    .cachedIn(viewModelScope)
                    .collect { pagingData ->
                        _dataFlow.value = pagingData
                    }
            }
    }


    private fun loadLocations(locationID: String, originID: String) {
        if (locationID.isNotEmpty()) {
            viewModelScope.launch {
                try {
                    val result = locationRepository.getLocationById(locationID.toInt(), networkStatus)
                    if (result != null) _location.value = result
                } catch (e: Exception) {

                }
            }
        }
        if (originID.isNotEmpty()) {
            viewModelScope.launch {
                try {
                    val result = locationRepository.getLocationById(originID.toInt(), networkStatus)
                    if (result != null) _origin.value = result
                } catch (e: Exception) {

                }
            }
        }
    }

    private var _character: MutableStateFlow<CharacterEntity> = MutableStateFlow(
        CharacterEntity(
            id = -1,
            name = "",
            status = "",
            gender = "",
            type = "",
            episode = emptyList(),
            image = "",
            species = "",
            origin = CharacterOrigin(name = "", url = ""),
            location = CharacterLocation(name = "", url = ""),
        )
    )

    private var _origin: MutableStateFlow<LocationEntity> = MutableStateFlow(
        LocationEntity(
            id = -1,
            name = "Unknown",
            type = "Unknown",
            dimension = "Unknown",
            emptyList()
        )
    )

    private var _location: MutableStateFlow<LocationEntity> = MutableStateFlow(
        LocationEntity(
            id = -1,
            name = "Unknown",
            type = "Unknown",
            dimension = "Unknown",
            emptyList()
        )
    )
}

