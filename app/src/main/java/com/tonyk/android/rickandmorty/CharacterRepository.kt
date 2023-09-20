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
    fun getCharacters(filter: CharacterFilter, pageSize: Int): Flow<PagingData<CharacterEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize
            ),
            remoteMediator = CharacterRemoteMediator(api, charactersDao, filter),
            pagingSourceFactory = { charactersDao.getAllCharacters() }
        )
            .flow
    }
}







