package com.tonyk.android.rickandmorty.api

import com.tonyk.android.rickandmorty.model.Character
import com.tonyk.android.rickandmorty.model.CharactersResponse

interface RickAndMortyApiRepository {
    suspend fun fetchCharacters(page : Int) : CharactersResponse
}