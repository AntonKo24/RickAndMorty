package com.tonyk.android.rickandmorty.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "locations")
class LocationEntity (
    @PrimaryKey val id: String,
    val name : String,
    val type : String,
    val dimension : String
)