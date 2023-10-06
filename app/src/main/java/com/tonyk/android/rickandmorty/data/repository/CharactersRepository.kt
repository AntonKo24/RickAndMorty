package com.tonyk.android.rickandmorty.data.repository

import androidx.paging.PagingData
import com.tonyk.android.rickandmorty.model.character.CharacterEntity
import com.tonyk.android.rickandmorty.model.character.CharacterFilter
import kotlinx.coroutines.flow.Flow

interface CharactersRepository {
    suspend fun getCharactersList(
        filter: CharacterFilter,
        status: Boolean
    ): Flow<PagingData<CharacterEntity>>

    suspend fun getCharacterListById(
        ids: List<String>,
        status: Boolean
    ): Flow<PagingData<CharacterEntity>>

    suspend fun getCharacterByID(
        id: Int,
        status: Boolean
    ): CharacterEntity

}
