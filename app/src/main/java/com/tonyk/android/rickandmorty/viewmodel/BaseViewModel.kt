package com.tonyk.android.rickandmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


abstract class BaseViewModel<T : Any>(
) : ViewModel() {
    private var _networkStatus: Boolean = false
    val networkStatus get() = _networkStatus

    private var alreadyLoaded = false

    protected val _dataFlow = MutableStateFlow<PagingData<T>>(PagingData.empty())
    val dataFlow: StateFlow<PagingData<T>> = _dataFlow.asStateFlow()



    fun initializeFragmentData(status: Boolean, dataLoading: () -> Unit) {
        if (status != _networkStatus) {
            _networkStatus = status
            _dataFlow.value = PagingData.empty()
            viewModelScope.coroutineContext.cancelChildren()
            dataLoading()
            alreadyLoaded = true
        } else if (!alreadyLoaded) {
            dataLoading()
            alreadyLoaded = true
        }
    }

    fun initializeDetails(status: Boolean, id: Int) {
        initializeFragmentData(status) {
            loadEntityData(id)
        }
    }

    fun initializeData(status: Boolean) {
        initializeFragmentData(status) {
            loadListData()
        }
    }

    fun refreshPage(status: Boolean, dataLoading: () -> Unit) {
        alreadyLoaded = false
        _dataFlow.value = PagingData.empty()
        _networkStatus = status
        viewModelScope.coroutineContext.cancelChildren()
        dataLoading()
    }
}



