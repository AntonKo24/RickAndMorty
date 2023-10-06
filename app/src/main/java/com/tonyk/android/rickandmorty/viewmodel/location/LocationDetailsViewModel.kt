package com.tonyk.android.rickandmorty.viewmodel.location

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.tonyk.android.rickandmorty.data.repository.CharactersRepository
import com.tonyk.android.rickandmorty.data.repository.LocationsRepository
import com.tonyk.android.rickandmorty.model.character.CharacterEntity
import com.tonyk.android.rickandmorty.model.location.LocationEntity
import com.tonyk.android.rickandmorty.util.conventers.extractIdsFromUrls
import com.tonyk.android.rickandmorty.viewmodel.base.BaseDetailViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LocationDetailsViewModel @Inject constructor(
    private val charactersRepository: CharactersRepository,
    private val locationsRepository: LocationsRepository
) : BaseDetailViewModel<CharacterEntity>() {

    val location: StateFlow<LocationEntity> get() = _location


    override fun loadListDetailsData(ids: List<String>) {
        if (ids.isNotEmpty()) {
            viewModelScope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        charactersRepository.getCharacterListById(ids, networkStatus)
                    }
                        .cachedIn(viewModelScope)
                        .collect { pagingData ->
                            _dataFlow.value = pagingData
                        }
                }
               catch (e: Exception) {
                   handleException(e)
               }
            }
        }
    }

    override fun loadEntityData(id: Int) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    locationsRepository.getLocationById(
                        id,
                        networkStatus
                    )
                }
                if (result != null) {
                    _location.value = result

                    val urls = result.residents
                    val idList = urls.extractIdsFromUrls()
                    withContext(Dispatchers.IO) { loadListDetailsData(idList) }
                }

            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    private var _location: MutableStateFlow<LocationEntity> = MutableStateFlow(
        LocationEntity(
            id = -1, name = "", residents = emptyList(), dimension = "", type = ""
        )
    )
}