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
    override fun getOfflineLocations(filter: LocationFilter): Flow<PagingData<LocationEntity>> {
        return Pager(
            config = PagingConfig(pageSize = Constants.PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {
                locationsDao.getAllLocations(
                    name = filter.name,
                    type = filter.type,
                    dimension = filter.dimension
                )
            }
        ).flow
    }

    override fun getOnlineLocations(filter: LocationFilter): Flow<PagingData<LocationEntity>> {
        return Pager(
            config = PagingConfig(pageSize = Constants.PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { LocationsPagingSource(api, locationsDao, filter) }
        ).flow
    }


}