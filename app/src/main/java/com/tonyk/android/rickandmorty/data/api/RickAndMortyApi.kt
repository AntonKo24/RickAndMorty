package com.tonyk.android.rickandmorty.data.api

import com.tonyk.android.rickandmorty.model.character.CharacterEntity
import com.tonyk.android.rickandmorty.model.character.CharactersResponse
import com.tonyk.android.rickandmorty.model.episode.EpisodeEntity
import com.tonyk.android.rickandmorty.model.episode.EpisodesResponse
import com.tonyk.android.rickandmorty.model.location.LocationEntity
import com.tonyk.android.rickandmorty.model.location.LocationsResponse
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

    @GET("character/{ids}")
    suspend fun fetchMultipleCharactersByID(@Path("ids") ids: List<String>): List<CharacterEntity>


    @GET("episode")
    suspend fun fetchAllEpisodes(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("episode") episode: String?
    ): EpisodesResponse

    @GET("episode/{ids}")
    suspend fun fetchMultipleEpisodesByID(@Path("ids") ids: List<String>): List<EpisodeEntity>


    @GET("location")
    suspend fun fetchAllLocations(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("type") type: String?,
        @Query("dimension") dimension: String?
    ): LocationsResponse

    @GET("location/{id}")
    suspend fun fetchLocationById(@Path("id") id: String): LocationEntity
}