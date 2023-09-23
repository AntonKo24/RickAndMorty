package com.tonyk.android.rickandmorty

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tonyk.android.rickandmorty.api.RickAndMortyApi
import com.tonyk.android.rickandmorty.database.CharactersDao
import com.tonyk.android.rickandmorty.model.CharacterEntity
import com.tonyk.android.rickandmorty.model.CharacterFilter
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalPagingApi::class)
class CharacterRepository(
    private val api: RickAndMortyApi,
    private val charactersDao: CharactersDao
) {
    fun getCharacters(filter: CharacterFilter): Flow<PagingData<CharacterEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = 20,
                prefetchDistance = 1
            ),
            remoteMediator = CharacterRemoteMediator(api, charactersDao, filter),
            pagingSourceFactory = {
                charactersDao.getAllCharacters(
                    name = filter.name,
                    status = filter.status,
                    species = filter.species,
                    type = filter.type,
                    gender = filter.gender
                )
            }
        )
            .flow
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}







