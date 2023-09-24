package com.tonyk.android.rickandmorty.model.character

import com.squareup.moshi.JsonClass
import com.tonyk.android.rickandmorty.model.PageInfo

@JsonClass(generateAdapter = true)
data class CharactersResponse(
    val results: List<CharacterEntity>,
    val info: PageInfo
)
