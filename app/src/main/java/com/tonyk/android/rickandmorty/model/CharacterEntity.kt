package com.tonyk.android.rickandmorty.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(tableName = "characters")
@JsonClass(generateAdapter = true)
data class CharacterEntity (
    @PrimaryKey val id : Int,
    @ColumnInfo(name = "name")val name : String,
    @ColumnInfo(name = "species")val species : String,
    @ColumnInfo(name = "status")val status : String,
    @ColumnInfo(name = "gender")val gender : String,
    @ColumnInfo(name = "image")val image : String,
    @ColumnInfo(name = "type")val type : String
    )