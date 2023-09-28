package com.tonyk.android.rickandmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tonyk.android.rickandmorty.model.episode.EpisodeEntity
import com.tonyk.android.rickandmorty.repositoryimpl.EpisodesRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    private val repository: EpisodesRepositoryImpl
) : ViewModel() {
    private var networkStatus: Boolean = false


    private val _episodes: MutableStateFlow<List<EpisodeEntity>> = MutableStateFlow(emptyList())
    val episodes: StateFlow<List<EpisodeEntity>> = _episodes

    fun getStatus(status: Boolean, ids: List<String>) {
        networkStatus = status
        episodesIDs = ids
        loadEpisodes()
    }

    private var episodesIDs: List<String> = emptyList()


    private fun loadEpisodes() {
        viewModelScope.launch {
            if (networkStatus) {
                val episodes = repository.fetchMultipleEpisodesByID(episodesIDs)
                _episodes.value = episodes

            } else {

            }
        }
    }
}