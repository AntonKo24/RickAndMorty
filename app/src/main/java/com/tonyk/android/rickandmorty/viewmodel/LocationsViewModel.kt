package com.tonyk.android.rickandmorty.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.tonyk.android.rickandmorty.model.location.LocationEntity
import com.tonyk.android.rickandmorty.model.location.LocationFilter
import com.tonyk.android.rickandmorty.repositoryimpl.LocationsRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationsViewModel @Inject constructor(
    private val repository: LocationsRepositoryImpl
) : BaseListViewModel<LocationEntity, LocationFilter>(LocationFilter()) {

    override fun loadListData() {
        viewModelScope.launch {
            repository.getLocationsList(_currentFilter, networkStatus)
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _data.value = pagingData
                }
        }
    }
}
