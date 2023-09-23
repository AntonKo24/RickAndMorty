package com.tonyk.android.rickandmorty.di

import com.tonyk.android.rickandmorty.CharacterRepositoryImpl
import com.tonyk.android.rickandmorty.data.api.RickAndMortyApi
import com.tonyk.android.rickandmorty.data.database.CharactersDao
import com.tonyk.android.rickandmorty.data.repository.CharacterRepository
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
    fun provideCharacterRepository(
        apiService: RickAndMortyApi,
        database: CharactersDao
    ): CharacterRepository {
        return CharacterRepositoryImpl(apiService, database)
    }
}
