package com.tonyk.android.rickandmorty.di

import com.tonyk.android.rickandmorty.repositoryimpl.CharactersRepositoryImpl
import com.tonyk.android.rickandmorty.data.api.RickAndMortyApi
import com.tonyk.android.rickandmorty.data.database.CharactersDao
import com.tonyk.android.rickandmorty.data.database.EpisodesDao
import com.tonyk.android.rickandmorty.data.database.LocationsDao
import com.tonyk.android.rickandmorty.data.repository.CharactersRepository
import com.tonyk.android.rickandmorty.data.repository.EpisodesRepository
import com.tonyk.android.rickandmorty.data.repository.LocationsRepository
import com.tonyk.android.rickandmorty.repositoryimpl.EpisodesRepositoryImpl
import com.tonyk.android.rickandmorty.repositoryimpl.LocationsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCharactersRepository(
        apiService: RickAndMortyApi,
        database: CharactersDao
    ): CharactersRepository {
        return CharactersRepositoryImpl(apiService, database)
    }

    @Provides
    @Singleton
    fun provideEpisodesRepository(
        apiService: RickAndMortyApi,
        database: EpisodesDao
    ): EpisodesRepository {
        return EpisodesRepositoryImpl(apiService, database)
    }
    @Provides
    @Singleton
    fun provideLocationsRepository(
        apiService: RickAndMortyApi,
        database: LocationsDao
    ): LocationsRepository {
        return LocationsRepositoryImpl(apiService, database)
    }

}
