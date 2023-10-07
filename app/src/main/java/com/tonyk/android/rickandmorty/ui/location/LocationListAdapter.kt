package com.tonyk.android.rickandmorty.ui.location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tonyk.android.rickandmorty.R
import com.tonyk.android.rickandmorty.databinding.LocationListItemBinding
import com.tonyk.android.rickandmorty.model.location.LocationEntity

class LocationViewHolder(
    private val binding: LocationListItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        locationEntity: LocationEntity,
        onLocationClicked: (location: LocationEntity) -> Unit
    ) {
        binding.apply {
            locationNameTxt.text =
                root.context.getString(R.string.location_name, locationEntity.name)
            locationTypeTxt.text =
                root.context.getString(R.string.location_type, locationEntity.type)
            locationDimensionTxt.text =
                root.context.getString(R.string.location_dimension, locationEntity.dimension)

            root.setOnClickListener { onLocationClicked(locationEntity) }
        }
    }
}

class LocationListAdapter(private val onLocationClicked: (location: LocationEntity) -> Unit) :
    PagingDataAdapter<LocationEntity, LocationViewHolder>(
        LocationDiffCallback()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return LocationViewHolder(LocationListItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val locationEntity = getItem(position)
        if (locationEntity != null) {
            holder.bind(locationEntity, onLocationClicked)
        }
    }
}

class LocationDiffCallback : DiffUtil.ItemCallback<LocationEntity>() {
    override fun areItemsTheSame(oldItem: LocationEntity, newItem: LocationEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: LocationEntity, newItem: LocationEntity): Boolean {
        return oldItem == newItem
    }
}