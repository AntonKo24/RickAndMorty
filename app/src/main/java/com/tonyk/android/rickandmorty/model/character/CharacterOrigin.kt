package com.tonyk.android.rickandmorty.model.character

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class CharacterOrigin(
    @ColumnInfo("originName") val name: String,
    @ColumnInfo("originUrl") val url: String
) : Parcelable