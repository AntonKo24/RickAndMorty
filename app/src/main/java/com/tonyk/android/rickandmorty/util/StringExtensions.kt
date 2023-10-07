package com.tonyk.android.rickandmorty.util

fun List<String>.extractIdsFromUrls(): List<String> {
    val idList = mutableListOf<String>()
    for (url in this) {
        val id = url.substringAfterLast("/")
        if (id.isNotEmpty()) {
            idList.add(id)
        }
    }
    return idList
}