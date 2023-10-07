package com.tonyk.android.rickandmorty.viewmodel.character

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.tonyk.android.rickandmorty.data.repository.CharactersRepository
import com.tonyk.android.rickandmorty.model.character.CharacterEntity
import com.tonyk.android.rickandmorty.model.character.CharacterFilter
import com.tonyk.android.rickandmorty.viewmodel.base.BaseListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CharactersListViewModel @Inject constructor(
    private val repository: CharactersRepository
) : BaseListViewModel<CharacterEntity, CharacterFilter>(CharacterFilter()) {

    override fun loadMainListData() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    repository.getCharactersListWithFilters(_currentFilter, networkStatus)
                        .cachedIn(viewModelScope)
                        .collect { pagingData ->
                            _dataFlow.value = pagingData
                        }
                }
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }
}