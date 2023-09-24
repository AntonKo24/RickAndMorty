package com.tonyk.android.rickandmorty.data.repository

import androidx.paging.PagingData
import com.tonyk.android.rickandmorty.model.character.CharacterFilter
import com.tonyk.android.rickandmorty.model.episode.EpisodeEntity
import com.tonyk.android.rickandmorty.model.episode.EpisodeFilter
import kotlinx.coroutines.flow.Flow

interface EpisodesRepository {
    fun getOfflineEpisodes(filter: EpisodeFilter): Flow<PagingData<EpisodeEntity>>
    fun getOnlineEpisodes(filter: EpisodeFilter): Flow<PagingData<EpisodeEntity>>
}