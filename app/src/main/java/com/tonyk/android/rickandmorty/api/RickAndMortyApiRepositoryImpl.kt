package com.tonyk.android.rickandmorty.api

import com.tonyk.android.rickandmorty.model.Character
import com.tonyk.android.rickandmorty.model.CharactersResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RickAndMortyApiRepositoryImpl @Inject constructor(private val rickAndMortyApi: RickAndMortyApi) : RickAndMortyApiRepository {
    override suspend fun fetchCharacters(page: Int): CharactersResponse {
        return rickAndMortyApi.fetchCharacters(page)
    }
}
