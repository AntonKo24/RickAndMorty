package com.tonyk.android.rickandmorty.ui.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.tonyk.android.rickandmorty.databinding.FragmentEpisodesFilterBinding
import com.tonyk.android.rickandmorty.databinding.FragmentLocationsFilterBinding
import com.tonyk.android.rickandmorty.model.location.LocationFilter
import com.tonyk.android.rickandmorty.viewmodel.EpisodesViewModel
import com.tonyk.android.rickandmorty.viewmodel.LocationsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationsFilterFragment : Fragment() {
    private var _binding: FragmentLocationsFilterBinding? = null
    private val binding get() = _binding!!
    private val locationsViewModel: LocationsViewModel by activityViewModels()

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


        val currentFilter = locationsViewModel.getCurrentFilter()


        binding.locationNamePicker.setText(currentFilter.name)
        binding.locationTypePicker.setText(currentFilter.type)
        binding.locationDimensionPicker.setText(currentFilter.dimension)

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.clearBtn.setOnClickListener {
            binding.locationNamePicker.text.clear()
            binding.locationTypePicker.text.clear()
            binding.locationDimensionPicker.text.clear()
        }

        binding.applyBtn.setOnClickListener {

            val locationName = binding.locationNamePicker.text.toString().takeIf { it.isNotEmpty() }
            val locationType = binding.locationTypePicker.text.toString().takeIf { it.isNotEmpty() }
            val locationDimension = binding.locationDimensionPicker.text.toString().takeIf { it.isNotEmpty() }

            val locationFilter = LocationFilter(
                name = locationName,
                type = locationType,
                dimension = locationDimension
            )

            locationsViewModel.applyFilter(locationFilter)
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}