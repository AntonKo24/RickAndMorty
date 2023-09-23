package com.tonyk.android.rickandmorty.data.api

import com.tonyk.android.rickandmorty.model.character.CharacterEntity
import com.tonyk.android.rickandmorty.model.character.CharactersResponse
import com.tonyk.android.rickandmorty.model.episode.EpisodesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {
    @GET("character")
    suspend fun fetchCharacters(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("status") status: String?,
        @Query("species") species: String?,
        @Query("type") type: String?,
        @Query("gender") gender: String?
    ): CharactersResponse

    @GET("character/{id}")
    suspend fun fetchCharacterByID(@Path("id") id: String): CharacterEntity

    @GET("character/{ids}")
    suspend fun fetchMultipleCharactersByID(@Path("ids") ids: List<String>): List<CharacterEntity>

    @GET("episode")
    suspend fun fetchAllEpisodes(
        @Query("page") page: Int,
        @Query("name") name : String?,
        @Query("episode") episode : String?
    ) : EpisodesResponse

    @GET("episode/{id}")
    suspend fun fetchEpisodeById(@Path("id") id: String): CharacterEntity

    @GET("episode")
    suspend fun fetchMultipleEpisodesByID(@Query("episode") id: List<String>)

    @GET("location")
    suspend fun fetchAllLocations(
        @Query("page")page: Int,
        @Query("name") name: String?,
        @Query("type") type: String?,
        @Query("dimension") dimension : String?
    )
}

