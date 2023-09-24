package com.tonyk.android.rickandmorty.util.pagingsources

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tonyk.android.rickandmorty.data.api.RickAndMortyApi
import com.tonyk.android.rickandmorty.data.database.LocationsDao
import com.tonyk.android.rickandmorty.model.location.LocationEntity
import com.tonyk.android.rickandmorty.model.location.LocationFilter
import com.tonyk.android.rickandmorty.util.Constants

class LocationsPagingSource(
    private val api: RickAndMortyApi,
    private val locationsDao: LocationsDao,
    private val filter: LocationFilter
) : PagingSource<Int, LocationEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LocationEntity> {
        val page = params.key ?: Constants.FIRST_PAGE_INDEX

        return try {
            val apiResponse =
                api.fetchAllLocations(
                    page = page,
                    name = filter.name,
                    type = filter.type,
                    dimension = filter.dimension
                )
            locationsDao.insertLocations(apiResponse.results)
            var nextPageNumber: Int? = null
            if (apiResponse.info.next != null) {
                val uri = Uri.parse(apiResponse.info.next)
                val nextPageQuery = uri.getQueryParameter("page")
                nextPageNumber = nextPageQuery?.toInt()
            }
            LoadResult.Page(
                data = apiResponse.results,
                prevKey = if (page == Constants.FIRST_PAGE_INDEX) null else page - 1,
                nextKey = nextPageNumber
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LocationEntity>): Int? {
        return null
    }
}