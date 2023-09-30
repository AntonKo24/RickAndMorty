package com.tonyk.android.rickandmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tonyk.android.rickandmorty.model.character.CharacterEntity
import com.tonyk.android.rickandmorty.repositoryimpl.CharactersRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EpisodeDetailViewModel @Inject constructor(
    private val repository: CharactersRepositoryImpl
) : ViewModel() {
    private var networkStatus: Boolean = false

    private val _characters = MutableStateFlow<PagingData<CharacterEntity>>(PagingData.empty())
    val characters: StateFlow<PagingData<CharacterEntity>> = _characters.asStateFlow()

    private var charactersIDs = emptyList<String>()

    fun getStatus(status: Boolean, ids: List<String>) {
        networkStatus = status
        charactersIDs = ids
        loadCharacter()
    }

    private fun loadCharacter() {
        viewModelScope.launch {
            if (charactersIDs.isNotEmpty()) {
                repository.getCharacterListById(charactersIDs, networkStatus)
                    .cachedIn(viewModelScope)
                    .collect { pagingData ->
                        _characters.value = pagingData
                    }
            }
        }
    }
}