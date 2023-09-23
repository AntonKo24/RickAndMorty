package com.tonyk.android.rickandmorty.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tonyk.android.rickandmorty.model.CharacterEntity
import com.tonyk.android.rickandmorty.model.EpisodeEntity
import com.tonyk.android.rickandmorty.model.LocationEntity


@Database(entities = [CharacterEntity::class, LocationEntity::class, EpisodeEntity::class], version = 1)
abstract class LocationsDatabase : RoomDatabase() {
    abstract fun charactersDao() : CharactersDao
    abstract fun locationsDao(): LocationsDao
    abstract fun episodesDao() : EpisodesDao
}
