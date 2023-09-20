package com.tonyk.android.rickandmorty

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.tonyk.android.rickandmorty.api.RickAndMortyApi
import com.tonyk.android.rickandmorty.database.CharactersDao
import com.tonyk.android.rickandmorty.model.CharacterEntity
import com.tonyk.android.rickandmorty.model.CharacterFilter


@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val api: RickAndMortyApi,
    private val charactersDao: CharactersDao,
    private val filter: CharacterFilter
) : RemoteMediator<Int, CharacterEntity>() {

    private var pageIndex = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {

        pageIndex = getPageIndex(loadType) ?: return MediatorResult.Success(endOfPaginationReached = true)


        return try {
            val characters = fetchCharacters(pageIndex)
            if (loadType == LoadType.REFRESH) {
                charactersDao.refresh(characters)
            } else {
                charactersDao.insertCharacters(characters)
            }
            Log.d("PAgingTest", "111. $pageIndex")
            MediatorResult.Success(

                endOfPaginationReached = characters.size < 20


            )
        } catch (e: Exception) {
            Log.d("PAgingTest", "222. $pageIndex")
            MediatorResult.Error(e)
        }

    }

    private fun getPageIndex(loadType: LoadType): Int? {
        return when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> null
            LoadType.APPEND -> pageIndex + 1
        }
    }


    private suspend fun fetchCharacters(page: Int): List<CharacterEntity> {
        return api.fetchCharacters(page).results
    }
}



