package com.tonyk.android.rickandmorty.di

import com.tonyk.android.rickandmorty.CharacterRepository
import com.tonyk.android.rickandmorty.api.RickAndMortyApi
import com.tonyk.android.rickandmorty.database.CharactersDao
import com.tonyk.android.rickandmorty.database.LocationsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class) // Указать правильный scope
object RepositoryModule {

    @Provides
    fun provideCharacterRepository(
        apiService: RickAndMortyApi,
        database: CharactersDao
    ): CharacterRepository {
        return CharacterRepository(apiService, database)
    }
}
