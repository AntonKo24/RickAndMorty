package com.tonyk.android.rickandmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


abstract class BaseDetailViewModel<T : Any>(
) : ViewModel() {
    private var _networkStatus: Boolean = false
    val networkStatus get() = _networkStatus
    private var alreadyLoaded = false

    protected val _dataFlow = MutableStateFlow<PagingData<T>>(PagingData.empty())
    val dataFlow: StateFlow<PagingData<T>> = _dataFlow.asStateFlow()

    private var _ids: List<String> = emptyList()
    val ids: List<String> get() = _ids

    fun getStatus(status: Boolean, idsInput: List<String>) {
        _ids = idsInput
        if (status != networkStatus) {
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

    abstract fun loadListData()
}
