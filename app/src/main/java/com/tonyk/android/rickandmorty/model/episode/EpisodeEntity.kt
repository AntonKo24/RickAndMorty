package com.tonyk.android.rickandmorty.model.episode

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


@Entity(tableName = "episodes")
@JsonClass(generateAdapter = true)
@Parcelize
data class EpisodeEntity (
    @PrimaryKey val id : String,
    val name : String,
    val episode : String,
    val air_date : String,
    val characters : List<String>
) : Parcelable