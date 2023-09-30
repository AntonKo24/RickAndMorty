package com.tonyk.android.rickandmorty.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tonyk.android.rickandmorty.data.api.RickAndMortyApi
import com.tonyk.android.rickandmorty.data.database.EpisodesDao
import com.tonyk.android.rickandmorty.data.repository.EpisodesRepository
import com.tonyk.android.rickandmorty.model.episode.EpisodeEntity
import com.tonyk.android.rickandmorty.model.episode.EpisodeFilter
import com.tonyk.android.rickandmorty.util.Constants
import com.tonyk.android.rickandmorty.util.pagingsources.EpisodesPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EpisodesRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi,
    private val episodesDao: EpisodesDao
) : EpisodesRepository {
    override suspend fun getListData(
        filter: EpisodeFilter,
        status: Boolean
    ): Flow<PagingData<EpisodeEntity>> {
        return Pager(
            config = PagingConfig(pageSize = Constants.PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {
                if (status) {
                    EpisodesPagingSource(api, episodesDao, filter)
                } else {
                    episodesDao.getAllEpisodes(
                        name = filter.name,
                        episode = filter.episode
                    )
                }

            }
        ).flow
    }

    override suspend fun getEpisodeListById(
        ids: List<String>,
        status: Boolean
    ): Flow<PagingData<EpisodeEntity>> {
        if (status) {
            val result = api.fetchMultipleEpisodesByID(ids)
            episodesDao.insertEpisodes(result)
        }
        return Pager(
            config = PagingConfig(pageSize = Constants.PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {
                episodesDao.getEpisodesByID(
                    id = ids
                )
            }
        ).flow
    }
}