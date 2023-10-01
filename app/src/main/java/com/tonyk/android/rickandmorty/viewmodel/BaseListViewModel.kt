package com.tonyk.android.rickandmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseListViewModel<T : Any, FilterType : Any>(
    defaultFilter: FilterType
) : ViewModel() {
    protected val _dataFlow = MutableStateFlow<PagingData<T>>(PagingData.empty())
    val dataFlow: StateFlow<PagingData<T>> = _dataFlow.asStateFlow()

    protected var _currentFilter: FilterType = defaultFilter

    private var _networkStatus: Boolean = false
    val networkStatus get() = _networkStatus

    fun getCurrentFilter(): FilterType {
        return _currentFilter
    }

    fun getStatus(status: Boolean) {
        _networkStatus = status
        loadListData()
    }

    fun refreshPage(status: Boolean) {
        if (status != networkStatus) {
            _networkStatus = status
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

