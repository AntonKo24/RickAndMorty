package com.tonyk.android.rickandmorty.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.tonyk.android.rickandmorty.model.character.CharacterEntity
import com.tonyk.android.rickandmorty.repositoryimpl.CharactersRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: CharactersRepositoryImpl
) : BaseDetailViewModel<CharacterEntity>() {

    override fun loadData() {
        if (ids.isNotEmpty()) {
            viewModelScope.launch {
                repository.getCharacterListById(ids, networkStatus)
                    .cachedIn(viewModelScope)
                    .collect { pagingData ->
                        _dataFlow.value = pagingData
                    }
            }
        }
    }
}
