package com.tonyk.android.rickandmorty

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tonyk.android.rickandmorty.data.api.RickAndMortyApi
import com.tonyk.android.rickandmorty.data.database.CharactersDao
import com.tonyk.android.rickandmorty.data.repository.CharacterRepository
import com.tonyk.android.rickandmorty.model.character.CharacterEntity
import com.tonyk.android.rickandmorty.model.character.CharacterFilter
import com.tonyk.android.rickandmorty.util.Constants.PAGE_SIZE
import com.tonyk.android.rickandmorty.util.pagingsources.CharactersPagingDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi,
    private val charactersDao: CharactersDao
) : CharacterRepository {
    override fun getOfflineCharacters(filter: CharacterFilter): Flow<PagingData<CharacterEntity>> {
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

    override fun getOnlineCharacters(filter: CharacterFilter): Flow<PagingData<CharacterEntity>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { CharactersPagingDataSource(api, charactersDao, filter) }
        ).flow
    }
}