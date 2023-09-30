package com.tonyk.android.rickandmorty.ui.location

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tonyk.android.rickandmorty.databinding.CharacterListItemBinding
import com.tonyk.android.rickandmorty.databinding.LocationListItemBinding
import com.tonyk.android.rickandmorty.model.character.CharacterEntity
import com.tonyk.android.rickandmorty.model.episode.EpisodeEntity
import com.tonyk.android.rickandmorty.model.location.LocationEntity

class LocationViewHolder(
    private val binding: LocationListItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        locationEntity: LocationEntity,
        onLocationClicked: (location: LocationEntity) -> Unit
    ) {
        binding.apply {
            locationNameTxt.text = locationEntity.name
            locationTypeTxt.text =  locationEntity.type
            locationDimensionTxt.text = locationEntity.dimension

            root.setOnClickListener { onLocationClicked(locationEntity) }
        }
    }
}

class LocationListAdapter(private val onEpisodeClicked: (location: LocationEntity) -> Unit) : PagingDataAdapter<LocationEntity, LocationViewHolder>(
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
            holder.bind(locationEntity, onEpisodeClicked)
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