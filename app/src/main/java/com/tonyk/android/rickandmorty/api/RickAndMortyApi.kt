package com.tonyk.android.rickandmorty.api

import com.tonyk.android.rickandmorty.model.Character
import com.tonyk.android.rickandmorty.model.CharactersResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {
    @GET("character")
    suspend fun fetchCharacters(@Query("page") page: Int): CharactersResponse
}
