package com.tonyk.android.rickandmorty.data.repository

import androidx.paging.PagingData
import com.tonyk.android.rickandmorty.model.location.LocationEntity
import com.tonyk.android.rickandmorty.model.location.LocationFilter
import kotlinx.coroutines.flow.Flow

interface LocationsRepository {
    suspend fun getLocationsListWithFilters(
        filter: LocationFilter,
        status: Boolean
    ): Flow<PagingData<LocationEntity>>

    suspend fun getLocationById(
        id: Int,
        status: Boolean
    ): LocationEntity?
}