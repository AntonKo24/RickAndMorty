package com.tonyk.android.rickandmorty.model.character

import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CharacterLocation (
    val name : String,
    val url : String
)