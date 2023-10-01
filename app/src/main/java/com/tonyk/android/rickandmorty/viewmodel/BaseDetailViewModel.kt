package com.tonyk.android.rickandmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


abstract class BaseDetailViewModel<T : Any>(
) : ViewModel() {
    private var _networkStatus: Boolean = false
    val networkStatus get() = _networkStatus

    protected val _dataFlow = MutableStateFlow<PagingData<T>>(PagingData.empty())
    val dataFlow: StateFlow<PagingData<T>> = _dataFlow.asStateFlow()

    private var _ids: List<String> = emptyList()
    val ids: List<String> get() = _ids

    fun getStatus(status: Boolean, idsInput: List<String>) {
        _networkStatus = status
        _ids = idsInput
        loadData()
    }

    abstract fun loadData()
}
