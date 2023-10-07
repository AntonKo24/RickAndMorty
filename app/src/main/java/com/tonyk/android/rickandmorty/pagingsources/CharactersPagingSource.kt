package com.tonyk.android.rickandmorty.pagingsources

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tonyk.android.rickandmorty.data.api.RickAndMortyApi
import com.tonyk.android.rickandmorty.data.database.CharactersDao
import com.tonyk.android.rickandmorty.model.character.CharacterEntity
import com.tonyk.android.rickandmorty.model.character.CharacterFilter
import com.tonyk.android.rickandmorty.util.Constants.FIRST_PAGE_INDEX
import javax.inject.Inject

class CharactersPagingDataSource @Inject constructor(
    private val api: RickAndMortyApi,
    private val charactersDao: CharactersDao,
    private val filter: CharacterFilter
) : PagingSource<Int, CharacterEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterEntity> {
        val page = params.key ?: FIRST_PAGE_INDEX
        return try {
            val apiResponse =
                api.fetchCharacters(
                    page = page,
                    name = filter.name,
                    status = filter.status,
                    species = filter.species,
                    type = filter.type,
                    gender = filter.gender,
                )
            charactersDao.insertCharacters(apiResponse.results)
            var nextPageNumber: Int? = null
            if (apiResponse.info.next != null) {
                val uri = Uri.parse(apiResponse.info.next)
                val nextPageQuery = uri.getQueryParameter("page")
                nextPageNumber = nextPageQuery?.toInt()
            }
            LoadResult.Page(
                data = apiResponse.results,
                prevKey = if (page == FIRST_PAGE_INDEX) null else page - 1,
                nextKey = nextPageNumber
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CharacterEntity>): Int? {
        return null
    }
}