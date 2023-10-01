package com.tonyk.android.rickandmorty.model.episode

import com.squareup.moshi.JsonClass
import com.tonyk.android.rickandmorty.model.PageInfo

@JsonClass(generateAdapter = true)
data class EpisodesResponse(
    val results: List<EpisodeEntity>,
    val info: PageInfo
)