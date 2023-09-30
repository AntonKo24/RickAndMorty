package com.tonyk.android.rickandmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tonyk.android.rickandmorty.model.character.CharacterFilter
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseListViewModel<T : Any, FilterType : Any>(
    defaultFilter: FilterType
) : ViewModel() {
    protected val _data = MutableStateFlow<PagingData<T>>(PagingData.empty())
    val dataFlow: StateFlow<PagingData<T>> = _data.asStateFlow()

    protected var _currentFilter: FilterType = defaultFilter

    protected var networkStatus: Boolean = false


    fun getCurrentFilter(): FilterType {
        return _currentFilter
    }

    fun getStatus(status: Boolean) {
        networkStatus = status
        loadListData()
    }

    fun refreshPage(status: Boolean) {
        if (status != networkStatus) {
            networkStatus = status
            viewModelScope.coroutineContext.cancelChildren()
            loadListData()
        }
    }

    abstract fun loadListData()

    fun applyFilter(filter: FilterType) {
        _currentFilter = filter
        loadListData()
    }
}
