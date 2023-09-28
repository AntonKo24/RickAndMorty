package com.tonyk.android.rickandmorty.model.character

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class CharacterLocation (
    @ColumnInfo("locationName")val name : String,
    @ColumnInfo("locationUrl")val url : String
) : Parcelable