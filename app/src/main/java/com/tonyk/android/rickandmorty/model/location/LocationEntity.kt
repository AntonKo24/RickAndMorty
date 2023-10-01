package com.tonyk.android.rickandmorty.model.location

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Entity(tableName = "locations")
@JsonClass(generateAdapter = true)
@Parcelize
data class LocationEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<String>
) : Parcelable