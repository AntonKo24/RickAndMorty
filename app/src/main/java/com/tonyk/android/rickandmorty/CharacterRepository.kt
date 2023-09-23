package com.tonyk.android.rickandmorty

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tonyk.android.rickandmorty.data.api.RickAndMortyApi
import com.tonyk.android.rickandmorty.data.database.CharactersDao
import com.tonyk.android.rickandmorty.model.CharacterEntity
import com.tonyk.android.rickandmorty.model.CharacterFilter
import com.tonyk.android.rickandmorty.util.Constants.PAGE_SIZE
import com.tonyk.android.rickandmorty.util.pagingsources.CharactersPagingDataSource
import kotlinx.coroutines.flow.Flow

class CharacterRepository(
    private val api: RickAndMortyApi,
    private val charactersDao: CharactersDao
) {
    fun getOfflineCharacters(filter: CharacterFilter): Flow<PagingData<CharacterEntity>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {
                charactersDao.getAllCharacters(
                    name = filter.name,
                    status = filter.status,
                    species = filter.species,
                    type = filter.type,
                    gender = filter.gender
                )
            }
        ).flow
    }

    fun getOnlineCharacters(filter: CharacterFilter): Flow<PagingData<CharacterEntity>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { CharactersPagingDataSource(api, charactersDao, filter) }
        ).flow
    }
}