package com.tonyk.android.rickandmorty.di

import android.content.Context
import androidx.room.Room
import com.tonyk.android.rickandmorty.data.database.CharactersDao
import com.tonyk.android.rickandmorty.data.database.EpisodesDao
import com.tonyk.android.rickandmorty.data.database.LocationsDao
import com.tonyk.android.rickandmorty.data.database.LocationsDatabase
import com.tonyk.android.rickandmorty.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LocationsDatabase {
        return Room.databaseBuilder(
            context,
            LocationsDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideCharactersDao(database: LocationsDatabase): CharactersDao {
        return database.charactersDao()
    }

    @Provides
    @Singleton
    fun provideEpisodesDao(database: LocationsDatabase): EpisodesDao {
        return database.episodesDao()
    }

    @Provides
    @Singleton
    fun provideLocationsDao(database: LocationsDatabase): LocationsDao {
        return database.locationsDao()
    }
}
