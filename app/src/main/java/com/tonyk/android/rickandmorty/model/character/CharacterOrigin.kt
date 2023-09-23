package com.tonyk.android.rickandmorty.model.character

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CharacterOrigin (
    val name : String,
    val url : String
)