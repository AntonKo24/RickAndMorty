package com.tonyk.android.rickandmorty.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tonyk.android.rickandmorty.R
import com.tonyk.android.rickandmorty.viewmodel.BaseListViewModel

abstract class BaseFilterFragment<T : Any> : Fragment() {

    abstract val viewModel: BaseListViewModel<T, *>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentFilter = viewModel.getCurrentFilter()

        setupFilterUI(currentFilter)

        view.findViewById<View>(R.id.clearBtn).setOnClickListener {
            clearFilterUI()
        }

        view.findViewById<View>(R.id.backBtn).setOnClickListener {
            findNavController().popBackStack()
        }

        view.findViewById<View>(R.id.applyButton).setOnClickListener {
            val newFilter = createFilterFromUI()
            applyFilter(newFilter)
            findNavController().popBackStack()
        }
    }

    abstract fun setupFilterUI(filter: Any)
    abstract fun clearFilterUI()
    abstract fun createFilterFromUI(): Any
    abstract fun applyFilter(filter: Any)
}

