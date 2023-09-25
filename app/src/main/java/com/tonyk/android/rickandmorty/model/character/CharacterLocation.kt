package com.tonyk.android.rickandmorty.model.character

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CharacterLocation (
    @ColumnInfo("locationName")val name : String,
    @ColumnInfo("locationUrl")val url : String
)