package com.tonyk.android.rickandmorty.model.character


data class CharacterFilter (
    val name: String? = null,
    val status: String? = null,
    val species: String? = null,
    val type: String? = null,
    val gender: String? = null,
    val id : List<String>? = null
)

