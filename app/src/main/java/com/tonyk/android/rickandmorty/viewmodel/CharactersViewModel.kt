package com.tonyk.android.rickandmorty.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.tonyk.android.rickandmorty.repositoryimpl.CharactersRepositoryImpl
import com.tonyk.android.rickandmorty.model.character.CharacterEntity
import com.tonyk.android.rickandmorty.model.character.CharacterFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val repository: CharactersRepositoryImpl
) : BaseListViewModel<CharacterEntity, CharacterFilter>(CharacterFilter()) {

    override fun loadListData() {
        viewModelScope.launch {
            try {
                repository.getCharactersList(_currentFilter, networkStatus)
                    .cachedIn(viewModelScope)
                    .collect { pagingData ->
                        _dataFlow.value = pagingData
                    }
                Log.d("debugio", "ЛОАД")
            }
            catch (e: Exception) {
                Log.d("debugio", "$e")
            }
        }
    }
}











