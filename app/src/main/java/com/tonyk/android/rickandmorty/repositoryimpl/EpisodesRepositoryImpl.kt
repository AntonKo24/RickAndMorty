package com.tonyk.android.rickandmorty.repositoryimpl

import androidx.paging.PagingData
import com.tonyk.android.rickandmorty.data.api.RickAndMortyApi
import com.tonyk.android.rickandmorty.data.database.EpisodesDao
import com.tonyk.android.rickandmorty.data.repository.EpisodesRepository
import com.tonyk.android.rickandmorty.model.character.CharacterFilter
import com.tonyk.android.rickandmorty.model.episode.EpisodeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EpisodesRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi,
    private val episodesDao: EpisodesDao
) : EpisodesRepository {
    override fun getOfflineEpisodes(filter: CharacterFilter): Flow<PagingData<EpisodeEntity>> {
        TODO("Not yet implemented")
    }

    override fun getOnlineEpisodes(filter: CharacterFilter): Flow<PagingData<EpisodeEntity>> {
        TODO("Not yet implemented")
    }

}