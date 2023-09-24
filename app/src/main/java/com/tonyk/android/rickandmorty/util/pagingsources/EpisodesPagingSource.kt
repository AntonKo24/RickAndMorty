package com.tonyk.android.rickandmorty.util.pagingsources

import android.net.Uri
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tonyk.android.rickandmorty.data.api.RickAndMortyApi
import com.tonyk.android.rickandmorty.data.database.EpisodesDao
import com.tonyk.android.rickandmorty.model.episode.EpisodeEntity
import com.tonyk.android.rickandmorty.model.episode.EpisodeFilter
import com.tonyk.android.rickandmorty.util.Constants.FIRST_PAGE_INDEX


class EpisodesPagingSource(
    private val api: RickAndMortyApi,
    private val episodesDao: EpisodesDao,
    private val filter: EpisodeFilter
) : PagingSource<Int, EpisodeEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EpisodeEntity> {
        val page = params.key ?: FIRST_PAGE_INDEX
        return try {
            val apiResponse =
                api.fetchAllEpisodes(
                    page = page,
                    name = filter.name,
                    episode = filter.episode
                )
            Log.d("PAgingTest33333", "$apiResponse")
            episodesDao.insertEpisodes(apiResponse.results)
            var nextPageNumber: Int? = null
            if (apiResponse.info.next != null) {
                val uri = Uri.parse(apiResponse.info.next)
                val nextPageQuery = uri.getQueryParameter("page")
                nextPageNumber = nextPageQuery?.toInt()
            }
            LoadResult.Page(
                data = apiResponse.results,
                prevKey = if (page == FIRST_PAGE_INDEX) null else page - 1,
                nextKey = nextPageNumber
            )

        } catch (e: Exception) {
            Log.d("PAgingTest33333", "ERROR")
            LoadResult.Error(e)

        }
    }

    override fun getRefreshKey(state: PagingState<Int, EpisodeEntity>): Int? {
        return null
    }
}