package com.tonyk.android.rickandmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tonyk.android.rickandmorty.model.location.LocationEntity
import com.tonyk.android.rickandmorty.model.location.LocationFilter
import com.tonyk.android.rickandmorty.repositoryimpl.LocationsRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationsViewModel @Inject constructor(
    private val repository: LocationsRepositoryImpl
) : ViewModel() {
    private val _locations = MutableStateFlow<PagingData<LocationEntity>>(PagingData.empty())
    val locations: StateFlow<PagingData<LocationEntity>> = _locations.asStateFlow()

    private var currentFilter: LocationFilter = LocationFilter()
    private var networkStatus: Boolean = false

    fun getStatus(status: Boolean) {
        networkStatus = status
        loadLocations() // Обновляем данные при изменении статуса сети
    }

    private fun loadLocations() {
        viewModelScope.launch {
            if (networkStatus) {
                repository.getOnlineLocations(currentFilter)
                    .cachedIn(viewModelScope)
                    .collect { pagingData ->
                        _locations.value = pagingData
                    }
            } else {
                repository.getOfflineLocations(currentFilter)
                    .cachedIn(viewModelScope)
                    .collect { pagingData ->
                        _locations.value = pagingData
                    }
            }
        }
    }

    fun applyFilter(filter: String) {
        currentFilter = LocationFilter(name = filter)
        loadLocations()
    }
}