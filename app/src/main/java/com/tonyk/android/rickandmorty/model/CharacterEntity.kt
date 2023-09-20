package com.tonyk.android.rickandmorty.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(tableName = "characters")
@JsonClass(generateAdapter = true)
data class CharacterEntity (
    @PrimaryKey val id : String,
    val name : String,
    val species : String,
    val status : String,
    val gender : String,
    val image : String,
    val type : String
    )