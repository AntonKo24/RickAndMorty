package com.tonyk.android.rickandmorty.model.character

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(tableName = "characters")
@JsonClass(generateAdapter = true)
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val species: String,
    val status: String,
    val gender: String,
    val image: String,
    val type: String,
    val episode: List<String>,
    @Embedded val origin: CharacterOrigin,
    @Embedded  val location: CharacterLocation
)