package com.tonyk.android.rickandmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tonyk.android.rickandmorty.api.RickAndMortyApiRepository
import com.tonyk.android.rickandmorty.model.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(private val repository: RickAndMortyApiRepository) :
    ViewModel() {

    private val _charactersList: MutableStateFlow<List<Character>> = MutableStateFlow(emptyList())
    val charactersList: StateFlow<List<Character>> = _charactersList

    private var currentPage = 1

    init {
        loadCharacters()
    }

    fun loadMoreCharacters() {
        currentPage++
        loadCharacters()
    }

    private fun loadCharacters() {
        viewModelScope.launch {
            val characters = repository.fetchCharacters(currentPage).results
            _charactersList.value += characters
        }
    }
}
