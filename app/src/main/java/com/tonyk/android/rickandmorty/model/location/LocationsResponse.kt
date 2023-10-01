package com.tonyk.android.rickandmorty.model.location

import com.squareup.moshi.JsonClass
import com.tonyk.android.rickandmorty.model.PageInfo

@JsonClass(generateAdapter = true)
data class LocationsResponse(
    val results: List<LocationEntity>,
    val info: PageInfo
)