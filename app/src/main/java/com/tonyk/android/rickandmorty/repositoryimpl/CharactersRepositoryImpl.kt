package com.tonyk.android.rickandmorty.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tonyk.android.rickandmorty.data.api.RickAndMortyApi
import com.tonyk.android.rickandmorty.data.database.CharactersDao
import com.tonyk.android.rickandmorty.data.repository.CharactersRepository
import com.tonyk.android.rickandmorty.model.character.CharacterEntity
import com.tonyk.android.rickandmorty.model.character.CharacterFilter
import com.tonyk.android.rickandmorty.util.Constants.PAGE_SIZE
import com.tonyk.android.rickandmorty.pagingsources.CharactersPagingDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CharactersRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi,
    private val charactersDao: CharactersDao
) : CharactersRepository {
    override suspend fun getCharactersList(
        filter: CharacterFilter,
        status: Boolean
    ): Flow<PagingData<CharacterEntity>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory =    {
                if (status) {
                    CharactersPagingDataSource(api, charactersDao, filter)
                } else {
                    charactersDao.getCharacters(
                        name = filter.name,
                        status = filter.status,
                        species = filter.species,
                        type = filter.type,
                        gender = filter.gender
                    )
                }
            }
        ).flow
    }

    override suspend fun getCharacterListById(
        ids: List<String>,
        status: Boolean
    ): Flow<PagingData<CharacterEntity>> {
        if (status) {
            val result = api.fetchMultipleCharactersByID(ids)
            charactersDao.insertCharacters(result)
        }
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {
                charactersDao.getCharactersByID(id = ids)
            }
        ).flow
    }

    override suspend fun getCharacterByID(id: Int, status: Boolean): CharacterEntity {
        if (status) {
            val result = api.fetchCharacterByID(id)
            charactersDao. insertCharacter(result)
        }
        return charactersDao.getCharacterByID(id)
    }
}