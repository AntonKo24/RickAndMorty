package com.tonyk.android.rickandmorty.ui.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.tonyk.android.rickandmorty.databinding.FragmentLocationsFilterBinding
import com.tonyk.android.rickandmorty.model.location.LocationEntity
import com.tonyk.android.rickandmorty.model.location.LocationFilter
import com.tonyk.android.rickandmorty.ui.base.BaseFragment
import com.tonyk.android.rickandmorty.viewmodel.location.LocationsListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationsFilterFragment : BaseFragment<LocationEntity>() {
    private var _binding: FragmentLocationsFilterBinding? = null
    private val binding get() = _binding!!

    override val viewModel: LocationsListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationsFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUi() {
        val currentFilter = viewModel.getCurrentFilter()

        binding.apply {
            locationNamePicker.setText(currentFilter.name)
            locationTypePicker.setText(currentFilter.type)
            locationDimensionPicker.setText(currentFilter.dimension)

            backBtn.setOnClickListener { findNavController().popBackStack() }
            clearBtn.setOnClickListener { clearFields() }
            applyBtn.setOnClickListener { applyFilter() }
        }
    }

    private fun clearFields() {
        binding.locationNamePicker.text.clear()
        binding.locationTypePicker.text.clear()
        binding.locationDimensionPicker.text.clear()
    }

    private fun applyFilter() {
        val locationFilter = createLocationFilter()
        viewModel.applyFilter(locationFilter)
        findNavController().popBackStack()
    }

    private fun createLocationFilter(): LocationFilter {
        val name = binding.locationNamePicker.text.toString().takeIf { it.isNotEmpty() }
        val type = binding.locationTypePicker.text.toString().takeIf { it.isNotEmpty() }
        val dimension = binding.locationDimensionPicker.text.toString().takeIf { it.isNotEmpty() }
        return LocationFilter(name = name, type = type, dimension = dimension)
    }
}
