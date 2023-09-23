package com.tonyk.android.rickandmorty.model

import dagger.Provides
import javax.inject.Inject


data class CharacterFilter @Inject constructor(
    val name: String? = null,
    val status: String? = null,
    val species: String? = null,
    val type: String? = null,
    val gender: String? = null
)

