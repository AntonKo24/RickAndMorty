package com.tonyk.android.rickandmorty.model.character

import androidx.room.ColumnInfo
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CharacterOrigin (
    @ColumnInfo("originName") val name : String,
    @ColumnInfo("originUrl") val url : String
)