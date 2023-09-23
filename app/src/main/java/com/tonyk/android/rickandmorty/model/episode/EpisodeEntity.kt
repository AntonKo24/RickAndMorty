package com.tonyk.android.rickandmorty.model.episode

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "episodes")
class EpisodeEntity (
    @PrimaryKey val id : String,
    val name : String,
    val episode : String,
    val air_date : String,
    val characters : List<String>
)