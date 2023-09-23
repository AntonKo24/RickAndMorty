package com.tonyk.android.rickandmorty.util

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.tonyk.android.rickandmorty.model.character.CharacterLocation


class CharacterLocationConverter {
    private val moshi = Moshi.Builder().build()

    @TypeConverter
    fun fromString(value: String?): CharacterLocation? {
        if (value == null) return null
        return moshi.adapter(CharacterLocation::class.java).fromJson(value)
    }

    @TypeConverter
    fun toString(location: CharacterLocation?): String? {
        if (location == null) return null
        return moshi.adapter(CharacterLocation::class.java).toJson(location)
    }
}
