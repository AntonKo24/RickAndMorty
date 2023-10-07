package com.tonyk.android.rickandmorty.model.episode

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(tableName = "episodes")
@JsonClass(generateAdapter = true)
data class EpisodeEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val episode: String,
    val air_date: String,
    val characters: List<String>
)