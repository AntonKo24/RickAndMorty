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

    private var alreadyLoaded = false

    fun initializeData(status: Boolean) {
        if (status != _networkStatus) {
            _networkStatus = status
            _dataFlow.value = PagingData.empty()
            viewModelScope.coroutineContext.cancelChildren()
            loadListData()
            alreadyLoaded = true
        }
        else if (!alreadyLoaded) {
            loadListData()
            alreadyLoaded = true
        }
    }

    fun refreshPage(status: Boolean) {
        _networkStatus = status
        _dataFlow.value = PagingData.empty()
        viewModelScope.coroutineContext.cancelChildren()
        loadListData()
    }

    fun getCurrentFilter(): FilterType {
        return _currentFilter
    }

    fun applyFilter(filter: FilterType) {
        _currentFilter = filter
        loadListData()
    }

    abstract fun loadListData()
}

