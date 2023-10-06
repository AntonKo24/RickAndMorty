package com.tonyk.android.rickandmorty.viewmodel.location

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.tonyk.android.rickandmorty.model.location.LocationEntity
import com.tonyk.android.rickandmorty.model.location.LocationFilter
import com.tonyk.android.rickandmorty.repositoryimpl.LocationsRepositoryImpl
import com.tonyk.android.rickandmorty.viewmodel.base.BaseListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LocationsListViewModel @Inject constructor(
    private val repository: LocationsRepositoryImpl
) : BaseListViewModel<LocationEntity, LocationFilter>(LocationFilter()) {

    override fun loadMainListData() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO)  { repository.getLocationsList(_currentFilter, networkStatus) }
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
