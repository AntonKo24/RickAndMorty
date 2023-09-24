package com.tonyk.android.rickandmorty.data.repository

import androidx.paging.PagingData
import com.tonyk.android.rickandmorty.model.character.CharacterEntity
import com.tonyk.android.rickandmorty.model.character.CharacterFilter
import kotlinx.coroutines.flow.Flow

interface CharactersRepository {
        fun getOfflineCharacters(filter: CharacterFilter): Flow<PagingData<CharacterEntity>>
        fun getOnlineCharacters(filter: CharacterFilter): Flow<PagingData<CharacterEntity>>
}
