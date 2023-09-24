package com.tonyk.android.rickandmorty.data.repository

import androidx.paging.PagingData
import com.tonyk.android.rickandmorty.model.character.CharacterFilter
import com.tonyk.android.rickandmorty.model.location.LocationEntity
import kotlinx.coroutines.flow.Flow

interface LocationsRepository {
    fun getOfflineLocations(filter: CharacterFilter): Flow<PagingData<LocationEntity>>
    fun getOnlineLocations(filter: CharacterFilter): Flow<PagingData<LocationEntity>>
}