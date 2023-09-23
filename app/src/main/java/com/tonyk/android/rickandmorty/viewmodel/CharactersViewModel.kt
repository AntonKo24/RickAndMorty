package com.tonyk.android.rickandmorty.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tonyk.android.rickandmorty.CharacterRepository
import com.tonyk.android.rickandmorty.model.CharacterEntity
import com.tonyk.android.rickandmorty.model.CharacterFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val repository: CharacterRepository
) : ViewModel() {
    private val _characters = MutableStateFlow<PagingData<CharacterEntity>>(PagingData.empty())
    val characters: StateFlow<PagingData<CharacterEntity>> = _characters.asStateFlow()

    private var currentFilter: CharacterFilter = CharacterFilter()
    private var networkStatus: Boolean = false

    fun getStatus(status: Boolean) {
        networkStatus = status
        loadCharacters() // Обновляем данные при изменении статуса сети
    }


    private fun loadCharacters() {
        viewModelScope.launch {
            if (networkStatus) {
                repository.getOnlineCharacters(currentFilter)
                    .cachedIn(viewModelScope)
                    .collect { pagingData ->
                        _characters.value = pagingData
                    }
            } else {
                repository.getOfflineCharacters(currentFilter)
                    .cachedIn(viewModelScope)
                    .collect { pagingData ->
                        _characters.value = pagingData
                    }
            }
        }
    }

    fun applyFilter(filter: String) {
        currentFilter = CharacterFilter(name = filter)
        loadCharacters()
    }
}










