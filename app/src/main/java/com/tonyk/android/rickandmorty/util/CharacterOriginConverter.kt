package com.tonyk.android.rickandmorty.util

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.tonyk.android.rickandmorty.model.character.CharacterOrigin

class CharacterOriginConverter {
    private val moshi = Moshi.Builder().build()

    @TypeConverter
    fun fromString(value: String?): CharacterOrigin? {
        if (value == null) return null
        return moshi.adapter(CharacterOrigin::class.java).fromJson(value)
    }

    @TypeConverter
    fun toString(origin: CharacterOrigin?): String? {
        if (origin == null) return null
        return moshi.adapter(CharacterOrigin::class.java).toJson(origin)
    }
}
