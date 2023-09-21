package com.tonyk.android.rickandmorty.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.flatMap
import androidx.paging.map
import com.tonyk.android.rickandmorty.CharacterRepository
import com.tonyk.android.rickandmorty.model.CharacterEntity
import com.tonyk.android.rickandmorty.model.CharacterFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val repository: CharacterRepository
) : ViewModel() {
    private val _characters = MutableStateFlow<PagingData<CharacterEntity>>(PagingData.empty())
    val characters: StateFlow<PagingData<CharacterEntity>> = _characters.asStateFlow()



    private var currentFilter: CharacterFilter = CharacterFilter()

    init {
        loadCharacters()
    }



    private fun loadCharacters() {
        viewModelScope.launch {
                repository.getCharacters(currentFilter)
                    .cachedIn(viewModelScope)
                    .collect { pagingData ->
                _characters.value = pagingData
            }
        }
    }

    fun applyFilter(filter : String) {
        currentFilter = CharacterFilter(name = filter)
        loadCharacters()
    }
}









