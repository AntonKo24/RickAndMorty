package com.tonyk.android.rickandmorty

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.tonyk.android.rickandmorty.api.RickAndMortyApi
import com.tonyk.android.rickandmorty.database.CharactersDao
import com.tonyk.android.rickandmorty.model.CharacterEntity
import com.tonyk.android.rickandmorty.model.CharacterFilter
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

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
            pagingSourceFactory = { charactersDao.getAllCharacters(
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







