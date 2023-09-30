package com.tonyk.android.rickandmorty.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tonyk.android.rickandmorty.data.api.RickAndMortyApi
import com.tonyk.android.rickandmorty.data.database.LocationsDao
import com.tonyk.android.rickandmorty.data.repository.LocationsRepository
import com.tonyk.android.rickandmorty.model.location.LocationEntity
import com.tonyk.android.rickandmorty.model.location.LocationFilter
import com.tonyk.android.rickandmorty.util.Constants
import com.tonyk.android.rickandmorty.util.pagingsources.LocationsPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationsRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi,
    private val locationsDao: LocationsDao
) : LocationsRepository {
    override suspend fun getLocationsList(
        filter: LocationFilter,
        status: Boolean
    ): Flow<PagingData<LocationEntity>> {
        return Pager(
            config = PagingConfig(pageSize = Constants.PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {
                if (status) {
                    LocationsPagingSource(api, locationsDao, filter)
                } else {
                    locationsDao.getAllLocations(
                        name = filter.name,
                        type = filter.type,
                        dimension = filter.dimension
                    )
                }

            }
        ).flow
    }

    override suspend fun getLocationById(id: String, status: Boolean): LocationEntity {
        if (status) {
            val result = api.fetchLocationById(id)
            locationsDao.insertLocation(result)
        }
        return locationsDao.getLocationByID(id)
    }
}