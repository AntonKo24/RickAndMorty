package com.tonyk.android.rickandmorty.util

import androidx.room.TypeConverter

class ListStringConverter {
    @TypeConverter
    fun fromStringList(value: String?): List<String>? {
        return value?.split(",")
    }

    @TypeConverter
    fun toStringList(list: List<String>?): String? {
        return list?.joinToString(",")
    }
}
