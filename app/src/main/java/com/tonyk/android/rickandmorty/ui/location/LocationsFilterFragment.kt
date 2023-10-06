package com.tonyk.android.rickandmorty.ui.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.tonyk.android.rickandmorty.databinding.FragmentLocationsFilterBinding
import com.tonyk.android.rickandmorty.model.location.LocationFilter
import com.tonyk.android.rickandmorty.viewmodel.location.LocationsListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationsFilterFragment : Fragment() {
    private var _binding: FragmentLocationsFilterBinding? = null
    private val binding get() = _binding!!
    private val locationsListViewModel: LocationsListViewModel by activityViewModels()

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

        val currentFilter = locationsListViewModel.getCurrentFilter()

        binding.apply {
            locationNamePicker.setText(currentFilter.name)
            locationTypePicker.setText(currentFilter.type)
            locationDimensionPicker.setText(currentFilter.dimension)

            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }

            clearBtn.setOnClickListener {
                locationNamePicker.text.clear()
                locationTypePicker.text.clear()
                locationDimensionPicker.text.clear()
            }

            applyBtn.setOnClickListener {
                val locationName = locationNamePicker.text.toString().takeIf { it.isNotEmpty() }
                val locationType = locationTypePicker.text.toString().takeIf { it.isNotEmpty() }
                val locationDimension =
                    locationDimensionPicker.text.toString().takeIf { it.isNotEmpty() }
                val locationFilter = LocationFilter(
                    name = locationName,
                    type = locationType,
                    dimension = locationDimension
                )
                locationsListViewModel.applyFilter(locationFilter)
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}