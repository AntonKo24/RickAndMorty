package com.tonyk.android.rickandmorty.model

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Character (
    val id : String,
    val name : String,
    val species : String,
    val status : String,
    val gender : String,
    val image : String,
    val type : String
    )