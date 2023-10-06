package com.tonyk.android.rickandmorty.viewmodel.base


abstract class BaseDetailViewModel<T : Any> : BaseViewModel<T>() {

    fun initializeDetailsFragment(status: Boolean, id: Int) {
        initializeFragmentData(status) {
            loadEntityData(id)
        }
    }

    fun refreshDetailsFragment(status: Boolean, id: Int) {
        refreshPage(status) {
            loadEntityData(id)
        }
    }

    abstract fun loadListDetailsData(ids: List<String>)

    abstract fun loadEntityData(id: Int)
}