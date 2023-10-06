package com.tonyk.android.rickandmorty.viewmodel.character

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.tonyk.android.rickandmorty.repositoryimpl.CharactersRepositoryImpl
import com.tonyk.android.rickandmorty.model.character.CharacterEntity
import com.tonyk.android.rickandmorty.model.character.CharacterFilter
import com.tonyk.android.rickandmorty.viewmodel.BaseListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject



@HiltViewModel
class CharactersListViewModel @Inject constructor(
    private val repository: CharactersRepositoryImpl
) : BaseListViewModel<CharacterEntity, CharacterFilter>(CharacterFilter()) {

    override fun loadListData() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                repository.getCharactersList(_currentFilter, networkStatus)
                    .cachedIn(viewModelScope)
                    .collect { pagingData ->
                        _dataFlow.value = pagingData
                    }
                }
            }
            catch (e: Exception) {
                Log.e("TestDebug", "${e.message}", e) // todo
            }
        }
    }
}











