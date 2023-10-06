package com.tonyk.android.rickandmorty.viewmodel.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<T : Any>(
) : ViewModel() {
    private var _networkStatus: Boolean = false
    val networkStatus get() = _networkStatus

    private var alreadyLoaded = false

    protected val _dataFlow = MutableStateFlow<PagingData<T>>(PagingData.empty())
    val dataFlow: StateFlow<PagingData<T>> = _dataFlow.asStateFlow()

    protected val _errorState: MutableSharedFlow<String> = MutableSharedFlow()
    val errorState: SharedFlow<String> = _errorState


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

    fun refreshPage(status: Boolean, dataLoading: () -> Unit) {
        alreadyLoaded = false
        _dataFlow.value = PagingData.empty()
        _networkStatus = status
        viewModelScope.coroutineContext.cancelChildren()
        dataLoading()
    }

    protected suspend fun handleException(e: Exception) {
        val errorMessage = "Something went wrong: ${e.message}"
        _errorState.emit(errorMessage)
        Log.e("ExceptionCatcher", errorMessage, e)
    }
}