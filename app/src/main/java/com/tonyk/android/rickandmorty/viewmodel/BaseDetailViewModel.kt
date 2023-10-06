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

    fun initializeFragmentData(status: Boolean, id: Int) {
        if (status != networkStatus) {
            _networkStatus = status
            _dataFlow.value = PagingData.empty()
            viewModelScope.coroutineContext.cancelChildren()
            loadEntityData(id)
            alreadyLoaded = true
        } else if (!alreadyLoaded) {
            loadEntityData(id)
            alreadyLoaded = true
        }
    }

    fun refreshPage(id: Int, status: Boolean) {
        alreadyLoaded = false
        _dataFlow.value = PagingData.empty()
        _networkStatus = status
        viewModelScope.coroutineContext.cancelChildren()
        initializeFragmentData(status, id)
    }

    abstract fun loadListData(ids: List<String>)

    abstract fun loadEntityData(id: Int)
}
