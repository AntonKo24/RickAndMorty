package com.tonyk.android.rickandmorty.data.repository

import androidx.paging.PagingData
import com.tonyk.android.rickandmorty.model.episode.EpisodeEntity
import com.tonyk.android.rickandmorty.model.episode.EpisodeFilter
import kotlinx.coroutines.flow.Flow

interface EpisodesRepository {
    suspend fun getEpisodeList(
        filter: EpisodeFilter,
        status: Boolean
    ): Flow<PagingData<EpisodeEntity>>

    suspend fun getEpisodeListById(
        ids: List<String>,
        status: Boolean
    ): Flow<PagingData<EpisodeEntity>>

}