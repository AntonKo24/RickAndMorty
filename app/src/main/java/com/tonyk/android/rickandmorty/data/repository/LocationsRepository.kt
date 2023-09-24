package com.tonyk.android.rickandmorty.data.repository

import androidx.paging.PagingData
import com.tonyk.android.rickandmorty.model.character.CharacterFilter
import com.tonyk.android.rickandmorty.model.location.LocationEntity
import com.tonyk.android.rickandmorty.model.location.LocationFilter
import kotlinx.coroutines.flow.Flow

interface LocationsRepository {
    fun getOfflineLocations(filter: LocationFilter): Flow<PagingData<LocationEntity>>
    fun getOnlineLocations(filter: LocationFilter): Flow<PagingData<LocationEntity>>
}