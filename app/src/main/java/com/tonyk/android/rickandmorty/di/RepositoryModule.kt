package com.tonyk.android.rickandmorty.di

import com.tonyk.android.rickandmorty.CharacterRepository
import com.tonyk.android.rickandmorty.api.RickAndMortyApi
import com.tonyk.android.rickandmorty.database.CharactersDao
import com.tonyk.android.rickandmorty.database.LocationsDatabase
import com.tonyk.android.rickandmorty.model.CharacterFilter
import dagger.Module
import dagger.Provides
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {



    @Provides
    @Singleton
    fun provideCharacterRepository(
        apiService: RickAndMortyApi,
        database: CharactersDao
    ): CharacterRepository {
        return CharacterRepository(apiService, database)
    }
}
