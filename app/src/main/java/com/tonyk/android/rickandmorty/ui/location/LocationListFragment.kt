package com.tonyk.android.rickandmorty.ui.location

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingDataAdapter
import com.tonyk.android.rickandmorty.model.location.LocationEntity
import com.tonyk.android.rickandmorty.ui.base.BaseListFragment
import com.tonyk.android.rickandmorty.viewmodel.location.LocationsListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationListFragment : BaseListFragment<LocationEntity, LocationViewHolder>() {

    override val viewModel: LocationsListViewModel by activityViewModels()

    override fun createAdapter(): PagingDataAdapter<LocationEntity, LocationViewHolder> {
        return LocationListAdapter(
            onLocationClicked = { location ->
                findNavController().navigate(
                    LocationListFragmentDirections.toLocationDetails(
                        location.id
                    )
                )
            }
        )
    }
    override fun navigateToFilterFragment() {
        findNavController().navigate(LocationListFragmentDirections.toEocationsFilterFragment())
    }
}