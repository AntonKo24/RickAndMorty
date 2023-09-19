package com.tonyk.android.rickandmorty.model

import android.view.inputmethod.EditorInfo
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CharactersResponse  (
    val results : List<Character>
)