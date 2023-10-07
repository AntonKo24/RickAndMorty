package com.tonyk.android.rickandmorty.viewmodel.episode

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.tonyk.android.rickandmorty.data.repository.CharactersRepository
import com.tonyk.android.rickandmorty.data.repository.EpisodesRepository
import com.tonyk.android.rickandmorty.model.character.CharacterEntity
import com.tonyk.android.rickandmorty.model.episode.EpisodeEntity
import com.tonyk.android.rickandmorty.util.extractIdsFromUrls
import com.tonyk.android.rickandmorty.viewmodel.base.BaseDetailViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class EpisodeDetailsViewModel @Inject constructor(
    private val charactersRepository: CharactersRepository,
    private val episodesRepository: EpisodesRepository
) : BaseDetailViewModel<CharacterEntity>() {

    val episode: StateFlow<EpisodeEntity> get() = _episode

    override fun loadListDetailsData(ids: List<String>) {
        if (ids.isNotEmpty()) {
            viewModelScope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        charactersRepository.getCharacterListById(
                            ids,
                            networkStatus
                        )
                    }
                        .cachedIn(viewModelScope)
                        .collect { pagingData ->
                            _dataFlow.value = pagingData
                        }
                } catch (e: Exception) {
                    handleException(e)
                }
            }
        }
    }

    override fun loadEntityData(id: Int) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    episodesRepository.getEpisodeByID(
                        id,
                        networkStatus
                    )
                }
                _episode.value = result

                val urls = result.characters
                val idList = urls.extractIdsFromUrls()
                withContext(Dispatchers.IO) { loadListDetailsData(idList) }

            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    private var _episode: MutableStateFlow<EpisodeEntity> = MutableStateFlow(
        EpisodeEntity(
            id = -1, name = "", characters = emptyList(), air_date = "", episode = ""
        )
    )
}