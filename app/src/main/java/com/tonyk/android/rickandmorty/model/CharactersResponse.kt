package com.tonyk.android.rickandmorty.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CharactersResponse(
    val results: List<CharacterEntity>,
    val info: PageInfo
)
