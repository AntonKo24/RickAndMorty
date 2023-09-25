package com.tonyk.android.rickandmorty.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tonyk.android.rickandmorty.model.character.CharacterEntity
import com.tonyk.android.rickandmorty.model.episode.EpisodeEntity
import com.tonyk.android.rickandmorty.model.location.LocationEntity
import com.tonyk.android.rickandmorty.util.CharacterLocationConverter
import com.tonyk.android.rickandmorty.util.CharacterOriginConverter
import com.tonyk.android.rickandmorty.util.conventers.ListStringConverter


@Database(entities = [CharacterEntity::class, LocationEntity::class, EpisodeEntity::class], version = 1, exportSchema = false)
@TypeConverters(
    ListStringConverter::class
)
abstract class LocationsDatabase : RoomDatabase() {
    abstract fun charactersDao() : CharactersDao
    abstract fun locationsDao(): LocationsDao
    abstract fun episodesDao() : EpisodesDao
}
