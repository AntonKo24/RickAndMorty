package com.tonyk.android.rickandmorty.viewmodel.base

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import kotlinx.coroutines.cancelChildren

abstract class BaseListViewModel<T : Any, FilterType : Any>(
    defaultFilter: FilterType
) : BaseViewModel<T>() {

    protected var _currentFilter: FilterType = defaultFilter

    fun initializeListFragment(status: Boolean) {
        initializeFragmentData(status) {
            loadMainListData()
        }
    }

    fun refreshListFragment(status: Boolean) {
        refreshPage(status) {
            loadMainListData()
        }
    }

    fun getCurrentFilter(): FilterType {
        return _currentFilter
    }

    fun applyFilter(filter: FilterType) {
        _dataFlow.value = PagingData.empty()
        _currentFilter = filter
        viewModelScope.coroutineContext.cancelChildren()
        loadMainListData()
    }

    abstract fun loadMainListData()
}