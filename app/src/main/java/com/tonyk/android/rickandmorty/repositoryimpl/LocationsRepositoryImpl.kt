package com.tonyk.android.rickandmorty.repositoryimpl

import androidx.paging.PagingData
import com.tonyk.android.rickandmorty.data.api.RickAndMortyApi
import com.tonyk.android.rickandmorty.data.database.LocationsDao
import com.tonyk.android.rickandmorty.data.repository.LocationsRepository
import com.tonyk.android.rickandmorty.model.character.CharacterFilter
import com.tonyk.android.rickandmorty.model.location.LocationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationsRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi,
    private val locationsDao: LocationsDao
) : LocationsRepository {
    override fun getOfflineLocations(filter: CharacterFilter): Flow<PagingData<LocationEntity>> {
        TODO("Not yet implemented")
    }

    override fun getOnlineLocations(filter: CharacterFilter): Flow<PagingData<LocationEntity>> {
        TODO("Not yet implemented")
    }
}