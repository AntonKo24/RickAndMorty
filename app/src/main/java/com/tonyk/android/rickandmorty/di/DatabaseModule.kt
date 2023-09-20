package com.tonyk.android.rickandmorty.di

import android.content.Context
import androidx.room.Room
import com.tonyk.android.rickandmorty.database.CharactersDao
import com.tonyk.android.rickandmorty.database.LocationsDatabase
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
            "locations_database"
        ).build()
    }

    @Provides
    fun provideCharactersDao(database: LocationsDatabase): CharactersDao {
        return database.charactersDao()
    }

    // Добавьте аналогичные методы для LocationsDao и EpisodesDao
}
