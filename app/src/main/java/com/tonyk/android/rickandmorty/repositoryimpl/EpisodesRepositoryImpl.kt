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
    override fun getOfflineEpisodes(filter: EpisodeFilter): Flow<PagingData<EpisodeEntity>> {
        return Pager(
            config = PagingConfig(pageSize = Constants.PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {
                episodesDao.getAllEpisodes(
                    name = filter.name,
                    episode = filter.episode
                )
            }
        ).flow
    }

    override fun getOnlineEpisodes(filter: EpisodeFilter): Flow<PagingData<EpisodeEntity>> {
        return Pager(
            config = PagingConfig(pageSize = Constants.PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { EpisodesPagingSource(api, episodesDao, filter) }
        ).flow
    }
}