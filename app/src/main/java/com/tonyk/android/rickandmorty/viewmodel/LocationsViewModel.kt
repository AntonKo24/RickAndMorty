package com.tonyk.android.rickandmorty.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tonyk.android.rickandmorty.model.episode.EpisodeFilter
import com.tonyk.android.rickandmorty.model.location.LocationEntity
import com.tonyk.android.rickandmorty.model.location.LocationFilter
import com.tonyk.android.rickandmorty.repositoryimpl.LocationsRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancelChildren
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

    private var _currentFilter: LocationFilter = LocationFilter()
    val currentFilter get() = _currentFilter

    private var networkStatus: Boolean = false

    fun getStatus(status: Boolean) {
        networkStatus = status
        loadLocations()
    }

    fun refreshPage(status: Boolean) {
        if (status != networkStatus) {
            networkStatus = status
            viewModelScope.coroutineContext.cancelChildren()
            loadLocations()
        }
    }

    private fun loadLocations() {
        viewModelScope.coroutineContext.cancelChildren()
        viewModelScope.launch {
                repository.getLocationsList(currentFilter, networkStatus)
                    .cachedIn(viewModelScope)
                    .collect { pagingData ->
                        _locations.value = pagingData
                    }
        }
        Log.d("TraceStatus", "$networkStatus")
    }

    fun applyFilter(filter: LocationFilter) {
        _currentFilter = filter
        loadLocations()
    }
}